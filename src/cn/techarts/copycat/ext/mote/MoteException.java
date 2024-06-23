package cn.techarts.copycat.ext.mote;

public class MoteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MoteException(String cause) {
		super(cause);
	}
	
	public static MoteException invalidSN() {
		return new MoteException("Illegal frame without device SN.");
	}
	
	public static MoteException itIsNotMote() {
		return new MoteException("Unrecognized protocol.");
	}
}
