package server;

import common.*;

public interface ServerHandler<M extends Msg> {
	
    public void handle(M msg, Connection c);
    
    public String getMsgType();
}