package com.liu.config;

import com.google.gson.Gson;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder.Text;
import jakarta.websocket.EndpointConfig;

public class MessageDecoder implements Text<Message> {

	private static Gson gson = new Gson();
	
	@Override
	public Message decode(String s) throws DecodeException {
		
		return gson.fromJson(s, Message.class);
	}

	@Override
	public boolean willDecode(String s) {
		
		return (s!=null);
	}
	
    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }

}
