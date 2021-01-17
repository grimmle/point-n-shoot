package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;
import handler.ServerHandler;

public class MsgManager {
	
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
    	
        for (ServerHandler h : handlers.get(m.getClass().getSimpleName())) {
            h.handle(m, c);
        }
    }
}