package cn.techarts.copycat.ext.mote;

public class ResponseFrame extends MoteFrame {
	
	public static final byte TYPE = 0X20;
	
	private byte status;
	
	public ResponseFrame(byte[] raw) {
		super(raw);
	}
	
	public ResponseFrame(String sn, byte status) {
		this.setSn(sn, NUL);
		this.status = status;
	}
	
	protected void parse() {
		super.parse();
		setStatus(payload[0]);
	}
	
	public byte[] encode() {
		var data = new byte[sn.length + 1];
		System.arraycopy(sn, 0, data, 0, sn.length);
		data[sn.length] = status;
		return serialize0(data, TYPE);
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}	
}
