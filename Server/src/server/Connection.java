package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import common.*;

public class Connection implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public int id;
	public boolean ready = false;
	private volatile boolean running = false;
	private MsgManager manager;

	private LinkedBlockingQueue<Msg> messages = new LinkedBlockingQueue<Msg>(); 

	public Connection(Socket socket, int id, MsgManager manager) {
		this.socket = socket;
		this.id = id;
		this.manager = manager;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			running = true;
			new Thread(() -> handleMessages()).start();
			while (running) {
				try {
					Msg data = (Msg) in.readObject();
					manager.received(data, this);
				} catch (EOFException e) {
					close();
					System.out.println("# client quit: closing connection");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					close();
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}
	
	public void handleMessages() {
		while(running) {
			try {
				Msg m = messages.take();
				out.writeUnshared(m);
				out.reset();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				close();
			}
		}
	}

	public void sendObject(Msg message) {
		messages.offer(message);
	}

	public void close() {
		try {
			running = false;
			in.close();
			out.close();
			socket.close();
			Server.connections.remove(this.id);
			ServerGame.players.remove(this.id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
