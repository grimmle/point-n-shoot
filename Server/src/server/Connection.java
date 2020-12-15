package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
//					System.out.println("# " + id + " received " + data.getClass().getSimpleName());
					manager.received(data, this);
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
	
	public void handleMessages() {
		while(running) {
			try {
				Msg m = messages.take();
				out.writeUnshared(m);
				out.reset();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
//			if(messageStack.size() > 0) {
//				Msg message = messageStack.get(0);
////				System.out.println("# handling msg " + message);
//				try {
//					if(message.getClass().getSimpleName().equals("AddConnectionMsg")) System.out.println("ADD "+ ((AddConnectionMsg)message).players);
////					if(message.getClass().getSimpleName().equals("MovePlayerMsg")) System.out.println("MOVE " + ((MovePlayerMsg)message).players);
////					out.writeObject(message);
//					out.writeUnshared(message);
//					out.reset();
//					messageStack.remove(0);
////					out.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}

	public void sendObject(Msg message) {
//		System.out.println("# send new msg: " + message);
//		messageStack.add(message);
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
