package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.*;
import handler.Handler;

public class MsgManager {
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
        for (Handler h : handlers.get(m.getClass().getSimpleName())) {
            h.handle(m);
        }
    }
}