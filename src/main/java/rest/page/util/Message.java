package rest.page.util;

import java.util.Date;
import java.util.List;

public class Message {
	private String Code;
	private String message;
	private Date date;
	private List list;
	
	public Message(String code, String message, Date date, List list) {
		super();
		Code = code;
		this.message = message;
		this.date = date;
		this.list = list;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Message(String code, String message, Date date) {
		super();
		Code = code;
		this.message = message;
		this.date = date;
	}
	public Message() {
		super();
	}
	
}
