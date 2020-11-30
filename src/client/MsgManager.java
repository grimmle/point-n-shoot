package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;
import server.ServerHandler;

public class MsgManager {
    static void send(Msg msg) {
        // os.print(msg.getName() / .getId() + ": " + msg.getParameters());
        // new DataOutputStream(os).writeInt(msg.getId());
        // new ObjectOutputStream(os).writeUnshared(msg);
    }

//  static Map<Class<? extends Msg>, List<Handler<? extends Msg>>> handlers;
    
    static Map<String, List<Handler<? extends Msg>>> handlers;

    public static void register(Handler<? extends Msg> h) {
    	List<Handler<? extends Msg>> list;
    	
    	if(handlers == null) {    		
    		Map map = new HashMap<>();
    		handlers = map;
    	}
    	
    	list = handlers.get(h.getMsgType());
    	if(list == null) list = new ArrayList<Handler<? extends Msg>>();
    	
        list.add(h);
        
        handlers.put(h.getMsgType(), list);
    }

    // received new message
    public void received(Msg m) {
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
    	
        for (Handler h : handlers.get(m.getClass().getSimpleName())) {
            h.handle(m);
        }
    }
}