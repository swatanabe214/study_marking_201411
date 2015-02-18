package jp.ktsystem.kadai201411.common;

public class KadaiException extends Exception {

	private static final long serialVersionUID = 1L;
	private int errorCode;

	/**
	 * <p>コンストラクタ</p>
	 * @param errorCode
	 */
	public KadaiException(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * <p>エラーコードを返します。</p>
	 * @return errorCode
	 */
	public int getErrorCode() {
		return this.errorCode;
	}

}
