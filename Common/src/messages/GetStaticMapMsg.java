package messages;

import common.Msg;

public class GetStaticMapMsg implements Msg {

	private static final long serialVersionUID = 8576917706186946304L;
	public int id;
	public Object staticMap;
	public Object dynamic;
}
