package rest.page.util;

import java.util.Date;

public class Message {
	private String Code;
	private String message;
	private Date date;
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
