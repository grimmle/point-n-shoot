package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import common.*;


public class Server implements Runnable {
	
	private int port;
	private boolean running = false;
	private ServerSocket serverSocket;
	private static int id = 0;
	
	private MsgManager manager;
	
	public static HashMap<Integer,Connection> connections = new HashMap<Integer,Connection>();

	public Server(int port) {
		this.port = port;
		this.manager = new MsgManager();
		// register handlers
		AddConnectionHandler add = new AddConnectionHandler();
		MsgManager.register(add);
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		running = true;
		System.out.println("# Server started on port: " + port); 

		while (running) {
			try {
				Socket socket = serverSocket.accept();
				//new client connection requested 
				initSocket(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdown();
	}

	private void initSocket(Socket socket) {
		System.out.println("New client request received : " + socket);
		// create client connection
		Connection connection = new Connection(socket, id, manager);
		connections.put(id, connection);
		new Thread(connection).start();
		id++;
	}

	public void shutdown() {
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server s = new Server(3000);
		s.start();
	}
}