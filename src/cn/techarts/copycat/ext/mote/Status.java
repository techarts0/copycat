package cn.techarts.copycat.ext.mote;


public enum Status {
	OK(0X00),
	FAILED(0X81), //129
	RESEND(0X82); //130
	
	private int status;
	
	Status(int status){
		this.setStatus(status);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
