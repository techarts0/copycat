package cn.techarts.copycat;

/**
 * An Exception
 */
public class Panic extends RuntimeException{
	private static final long serialVersionUID = -4200614776037388222L;
	
	public Panic() {
		super();
	}
	
	public Panic(Exception e) {
		super(e);
	}
	
	public Panic(String cause) {
		super(cause);
	}
	
	public Panic(Exception e, String cause) {
		super(cause, e);
	}
	
	public Panic(Throwable e, String cause) {
		super(cause, e);
	}
	
	public static Panic nullBuffer() {
		return new Panic("Buffer is null.");
	}
	
	public static Panic nullAction() {
		return new Panic("The CLI action is required.");
	}
}
