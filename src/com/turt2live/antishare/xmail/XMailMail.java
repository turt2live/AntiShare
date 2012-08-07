package com.turt2live.antishare.xmail;

import com.turt2live.xmail.api.XMailAPI;

public class XMailMail {

	private String to, message;

	public XMailMail(String to, String message){
		this.to = to;
		this.message = message;
	}

	public void send(){
		XMailAPI api = new XMailAPI();
		api.send(to, message);
	}

}
