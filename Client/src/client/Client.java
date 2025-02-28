package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import common.*;
import handler.*;
import messages.AddConnectionMsg;
import messages.GetStaticMapMsg;
import messages.RemoveConnectionMsg;

public class Client implements Runnable {

	private String host;
	private int port;

	private static Socket socket;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;

	private volatile static boolean running = false;
	private MsgManager manager;
	
	public static int id = -1;


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
			GetStaticMapHandler map = new GetStaticMapHandler();
			MovePlayerMsgHandler move = new MovePlayerMsgHandler();
			DynamicObjectsUpdateHandler dyn = new DynamicObjectsUpdateHandler();
			
			MsgManager.register(add);
			MsgManager.register(map);
			MsgManager.register(move);
			MsgManager.register(dyn);
			
			new Thread(this).start();
		} catch (ConnectException e) {
			System.out.println("> Unable to connect to the server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			running = false;
			RemoveConnectionMsg msg = new RemoveConnectionMsg();
			msg.id = id;
			sendObject(msg);
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendObject(Msg msg) {
		try {
//			System.out.println("client sent " + msg);
			out.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		sendObject(new AddConnectionMsg());
		sendObject(new GetStaticMapMsg());
		try {
			running = true;
			while (running) {
				try {
					Msg data = (Msg) in.readObject();
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
	}

}