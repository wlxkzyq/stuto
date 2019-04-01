package com.stuto.core.pub;

import java.util.List;

/** 
* 统一返回实体类 
* @author 作者 : zyq
* 创建时间：2017年5月19日 下午3:35:33 
* @version 1.0
*/
public class OutputObject {
	
	/**
	 * 返回状态码
	 * 0 : 成功
	 * 其它 ： 失败
	 */
	private int code;
	
	/**
	 * 返回描述信息
	 */
	private String msg;
	
	/**
	 * 返回警告信息
	 */
	private List<String> warnings;
	
	/**
	 * 返回单个对象
	 */
	private Object bean;
	
	/**
	 * 返回集合对象
	 */
	private List<? extends Object> rows;
	
	private int total;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}


	public List<? extends Object> getRows() {
		return rows;
	}

	public void setRows(List<? extends Object> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
