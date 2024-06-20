package cn.techarts.copycat.ext.mars;

public enum Status {
	SUCCESSFUL(0X00),
	FAILED(0X01),
	RESEND(0X02);
	
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
