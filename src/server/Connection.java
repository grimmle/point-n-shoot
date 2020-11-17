package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.*;

public class Connection implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public int id;
	private boolean running = false;
	private MsgManager manager;

	public Connection(Socket socket, int id, MsgManager manager) {
		this.socket = socket;
		this.id = id;
		this.manager = manager;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
//			listener = new EventListener();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			running = true;
			System.out.println("starting connection");
			while (running) {
				try {
					Msg data = (Msg) in.readObject();
					System.out.println("# received " + data);
					// listener.received(data, this);
					manager.received(data);
				} catch (EOFException e) {
					System.out.println("# client quit: closing connection");
					close();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendObject(Msg message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			running = false;
			in.close();
			out.close();
			socket.close();
			Server.connections.remove(this.id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
