package cn.techarts.copycat.ext.mote;

public enum Precision {
	NUL((char)0X00),
	SEC((char)0X04),
	MS((char)0X08);
	
	private char size = 0X00;
	
	Precision(char size){
		this.setSize(size);
	}

	public char getSize() {
		return size;
	}

	public void setSize(char size) {
		this.size = size;
	}
	
	public char getPrecision() {
		return this.size;
	}
	
	public byte toByte() {
		return (byte)this.size;
	}
}
