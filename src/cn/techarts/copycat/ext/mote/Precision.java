package cn.techarts.copycat.ext.mote;

public enum Precision {
	NUL((char)0),
	SEC((char)4),
	MS((char)8);
	
	private char size = 0;
	
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
}
