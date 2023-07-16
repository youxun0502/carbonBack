package com.liu.config;

import com.google.gson.Gson;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder.Text;
import jakarta.websocket.EndpointConfig;

public class MessageEncoder implements Text<Message> {

	private Gson gson = new Gson();
	
	@Override
	public String encode(Message message) throws EncodeException {
		return gson.toJson(message);
	}
	
	@Override
	public void init(EndpointConfig endpointConfig) {
		
	}
	
	@Override
	public void destroy() {
		
	}

}
