package cn.techarts.copycat.ext.mote;


public enum Status {
	OK((byte)0X00),
	FAILED((byte)0X81), //129
	RESEND((byte)0X82); //130
	
	private byte status;
	
	Status(byte status){
		this.setStatus(status);
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
}
