package handler;

import common.*;
import server.Connection;

public interface ServerHandler<M extends Msg> {
	
    public void handle(M msg, Connection c);
    
    public String getMsgType();
}