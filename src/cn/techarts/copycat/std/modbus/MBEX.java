package cn.techarts.copycat.std.modbus;

/**
 * MODBUS Exception Codes (SLAVE = SERVER)
 */
public enum MBEX {
	ILLEGAL_FUNCTION(1),
	ILLEGAL_DATA_ADDRESS(2),
	ILLEGAL_DATA_VALUE(3),
	SLAVE_DEVICE_FAILURE(4),
	SLAVE_ACKNOWLEDGE(5),
	SLAVE_DEVICE_BUSY(6),
	MEMORY_PARITY_ERROR(7),
	GATEWAY_INVALID_PATH(0x0a),
	GATEWAY_NO_RESPONDING(0x0b);

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
