package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Block;
import common.GameObject;
import common.Pickup;
import common.Player;
import handler.AddConnectionHandler;
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
		
		MsgManager.register(add);
		MsgManager.register(map);
		MsgManager.register(move);
		
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
		
		//setup game
		initGame();

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

	private void initGame() {
		ServerGame.staticMap.add(new Block(100, 250));
		ServerGame.staticMap.add(new Block(320, 760));
		ServerGame.staticMap.add(new Block(580, 20));
		ServerGame.staticMap.add(new Pickup(150, 250));
		ServerGame.staticMap.add(new Pickup(550, 250));
		ServerGame.staticMap.add(new Pickup(275, 300));
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