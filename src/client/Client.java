package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import common.*;

public class Client implements Runnable {

	private String host;
	private int port;

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private boolean running = false;
//	private EventListener listener;
	private MsgManager manager;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			manager = new MsgManager();
			//register handlers
			AddConnectionHandler add = new AddConnectionHandler();
			MsgManager.register(add);
			
//			listener = new EventListener();
			new Thread(this).start();
		} catch (ConnectException e) {
			System.out.println("> Unable to connect to the server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			running = false;
			RemoveConnectionMsg msg = new RemoveConnectionMsg();
			sendObject(msg);
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendObject(Msg msg) {
		try {
			out.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			running = true;
			new Game();
			while (running) {
				try {
					Msg data = (Msg) in.readObject();
					System.out.println("> Client received this: " + data);
					manager.received(data);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client c = new Client("localhost", 3000);
		c.connect();
		c.sendObject(new AddConnectionMsg());
	}

}