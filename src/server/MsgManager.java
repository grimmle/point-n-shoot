package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;

public class MsgManager {
    static void send(Msg msg) {
        // os.print(msg.getName() / .getId() + ": " + msg.getParameters());
        // new DataOutputStream(os).writeInt(msg.getId());
        // new ObjectOutputStream(os).writeUnshared(msg);
    }

//    static Map<Class<? extends Msg>, List<Handler<? extends Msg>>> handlers;
    static Map<String, List<ServerHandler<? extends Msg>>> handlers;

    public static void register(ServerHandler<? extends Msg> h) {
    	List<ServerHandler<? extends Msg>> list;
    	
    	if(handlers == null) {    		
    		Map map = new HashMap<>();
    		handlers = map;
    	}
    	
    	list = handlers.get(h.getMsgType());
    	if(list == null) list = new ArrayList<ServerHandler<? extends Msg>>();
    	
        list.add(h);
        
        handlers.put(h.getMsgType(), list);
    }

    // received new message
    public void received(Msg m, Connection c) {
    	System.out.println(m.getClass().getSimpleName());
    	System.out.println(handlers);
    	
//    	if(m.getClass().getSimpleName().equals("AddConnectionMsg")) {
//    		AddConnectionMsg msg = (AddConnectionMsg)m;
//    		
//    		Server.connections.forEach((i, c) -> {
//    			System.out.println("send msg to " + i);
//    			System.out.println(c.id);
//    			c.sendObject(new AddConnectionMsg());
//    		});
//    	}
    	
        for (ServerHandler h : handlers.get(m.getClass().getSimpleName())) {
            h.handle(m, c);
        }
    }
}