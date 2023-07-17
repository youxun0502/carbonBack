package com.liu.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liu.model.Chat;
import com.liu.model.Member;
import com.liu.service.ChatService;
import com.liu.service.MemberService;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/chat/{fromUserId}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndPoint {

	private Session session;

	private static Set<ChatEndPoint> chatEndPoints = new CopyOnWriteArraySet<>();// 將被所有ChatEndPoint共享

	private static Map<String, String> users = new HashMap<>();

	//收訊方的會話
	private static Map<String,Session> userSessions = new ConcurrentHashMap<>();
	
	@OnOpen
	public void onOpen(Session session, @PathParam("fromUserId") String fromUserId) {

		this.session = session;

		chatEndPoints.add(this);

		users.put(session.getId(), fromUserId);
		
		userSessions.put(fromUserId, session);//傳訊方的會話

//		Message message = new Message();
//		message.setFrom(userId);
//		message.setContent("Connected!");
//		broadCast(message);
	}
	

	@OnMessage
	public void onMessage(Message message) {
	
		System.out.println(message.getFrom());
		System.out.println(message.getTo());
		sendMessageToUser(message);//發訊息
	}

	@OnClose
	public void onClose() {
		chatEndPoints.remove(this);
	}
	
	
	@OnError
	public void onError(Session session, Throwable throwable) {
		System.out.println(throwable.toString());
	}

//	// 群發
//	private static void broadCast(Message message) {
//
//		chatEndPoints.forEach(endpoint -> {
//			synchronized (endpoint) { // synchronized 確保多線程下的同步訪問
//				try {
//					// 獲取與連結相關的會話 //返回一個RemoteEndPoint.Basic物件,用已發送基本消息
//					endpoint.session.getBasicRemote().sendObject(message);
//				} catch (IOException | EncodeException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	// 一對一聊天室
	private static void sendMessageToUser(Message message) {
	
		Session session = userSessions.get(message.getTo());
		if (session != null && session.isOpen()) {
			try {
				session.getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException e) {
				e.printStackTrace();
			}
		}
	}
}
