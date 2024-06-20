package cn.techarts.copycat.ext.mars;

public class ResponseFrame extends MarsFrame {
	
	public static final byte TYPE = 0X20;
	
	private byte status;
	
	protected void parse() {
		super.parse();
		setStatus(payload[0]);
	}
	
	public byte[] serialize() {
		return serialize0(new byte[] {status});
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
}
