package cn.techarts.copycat.std.modbus;

/**
 * MODBUS Exception Codes
 */
public enum MBEX {
	ILLEGAL_FUNCTION(1),
	ILLEGAL_DATA_ADDRESS(2),
	ILLEGAL_DATA_VALUE(3),
	SLAVE_DEVICE_FAILURE(4),
	ACKNOWLEDGE(5),
	SLAVE_DEVICE_BUSY(6),
	MEMORY_PARITY_ERROR(7),
	GATEWAY_PATH_UNAVAILABLE(0x0a),
	GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND(0x0b);

	private int code;
	
	MBEX(int errorCode) {
		this.setCode(errorCode);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
