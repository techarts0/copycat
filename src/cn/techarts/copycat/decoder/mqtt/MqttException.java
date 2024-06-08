package cn.techarts.copycat.decoder.mqtt;

public class MqttException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MqttException() {
		super();
	}
	
	public MqttException(Exception e) {
		super(e);
	}
	
	public MqttException(String cause) {
		super(cause);
	}
	
	public MqttException(Exception e, String cause) {
		super(cause, e);
	}
	
	public MqttException(Throwable e, String cause) {
		super(cause, e);
	}

}
