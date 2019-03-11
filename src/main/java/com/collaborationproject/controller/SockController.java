package com.collaborationproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collaborationproject.model.Chat;


@RestController
public class SockController {
	/*private final SimpMessagingTemplate messagingTemplate;
	private List<String> users = new ArrayList<String>();
	public void onLogout(String user){
		users.remove(user);
		messagingTemplate.convertAndSend("/topic/leave",user);
	}	
	@Autowired
	public SockController(SimpMessagingTemplate messagingTemplate){
		this.messagingTemplate=messagingTemplate;
	}
	
	@SubscribeMapping("/join/{userName}")
	public List<String> join(@DestinationVariable("userName") String userName){
		if(!users.contains(userName))
		users.add(userName);
		messagingTemplate.convertAndSend("/topic/join",userName);
		return users;
	}
	
	@MessageMapping("/chat")
	public void chatReveived(Chat chat){
		if("all".equals(chat.getTo()))
			messagingTemplate.convertAndSend("/queue/chats",chat);
		else{
			messagingTemplate.convertAndSend("/queue/chats/"+chat.getTo(),chat);
			messagingTemplate.convertAndSend("/queue/chats/"+chat.getFrom(),chat);
			}
	}*/
}
