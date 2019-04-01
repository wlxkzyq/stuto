package com.stuto.core.pub;
/**
* 框架运行时异常
* @author 作者 : zyq
* 创建时间：2017年4月25日 下午9:37:55
* @version 1.0
*/
public class StutoRuntimeException extends RuntimeException{

	/**
	 * 异常代码
	 */
	private String code;

	/**
	 * 异常描述
	 */
	private String msg;

	public StutoRuntimeException(){
		super();
	}

	public StutoRuntimeException(String code){
		super(code);
		this.code=code;
	}

	public StutoRuntimeException(String code,String msg){
		super(code+":"+msg);
		this.code=code;
		this.msg=msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



}
