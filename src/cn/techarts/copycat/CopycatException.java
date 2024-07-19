package cn.techarts.copycat;

public class CopycatException extends RuntimeException{
	private static final long serialVersionUID = -4200614776037388222L;
	
	public CopycatException() {
		super();
	}
	
	public CopycatException(Exception e) {
		super(e);
	}
	
	public CopycatException(String cause) {
		super(cause);
	}
	
	public CopycatException(Exception e, String cause) {
		super(cause, e);
	}
	
	public CopycatException(Throwable e, String cause) {
		super(cause, e);
	}
	
	public static CopycatException NullBuffer() {
		return new CopycatException("Buffer is null.");
	}
}
