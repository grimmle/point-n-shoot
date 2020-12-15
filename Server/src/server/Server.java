package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Block;
import common.GameObject;
import common.Pickup;
import handler.AddConnectionHandler;
import handler.DynamicObjectsUpdateHandler;
import handler.GetStaticMapHandler;
import handler.MovePlayerMsgHandler;


public class Server implements Runnable {
	
	private int port;
	private boolean running = false;
	private ServerSocket serverSocket;
	private MsgManager manager;
	private static int id = 0;
	
	public static ConcurrentHashMap<Integer,Connection> connections = new ConcurrentHashMap<Integer,Connection>();
	
	public static ServerGame game;

	public Server(int port) {
		this.port = port;
		this.manager = new MsgManager();
		
		// register handlers
		AddConnectionHandler add = new AddConnectionHandler();
		GetStaticMapHandler map = new GetStaticMapHandler();
		MovePlayerMsgHandler move = new MovePlayerMsgHandler();
		DynamicObjectsUpdateHandler dyn = new DynamicObjectsUpdateHandler();
		
		MsgManager.register(add);
		MsgManager.register(map);
		MsgManager.register(move);
		MsgManager.register(dyn);
		
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
	public synchronized void run() {
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
		if(connections.size() == 1) new ServerGame().start();
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