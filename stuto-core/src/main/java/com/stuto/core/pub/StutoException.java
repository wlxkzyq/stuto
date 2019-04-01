package com.stuto.core.pub;
/** 
* 框架封装异常
* @author 作者 : zyq
* 创建时间：2017年4月25日 下午9:33:07 
* @version 1.0
*/
public class StutoException extends Exception{

	/**
	 * 异常代码
	 */
	private String code;
	
	/**
	 * 异常描述
	 */
	private String msg;
	
	public StutoException(){
		super();
	}
	
	public StutoException(String msg){
		super(msg);
		this.msg=msg;
	}
	
	public StutoException(String code,String msg){
		super(msg);
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
