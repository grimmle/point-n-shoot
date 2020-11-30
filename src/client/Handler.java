package client;

import common.Msg;

public interface Handler<M extends Msg> {
	
    public void handle(M msg);
    
    public String getMsgType();
}