package common;

public interface Handler<M extends Msg> {
	
    public void handle(M msg);
    
    public String getMsgType();
}