package com.collaborationproject.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.collaborationproject.model.Error;
import com.collaborationproject.dao.FriendsDAO;
import com.collaborationproject.model.Friends;
import com.collaborationproject.model.UserDetails;

@RestController
public class FriendsController {
	@Autowired
	FriendsDAO friendsDAO;
	
	@RequestMapping(value="/getSuggestedFriends",method=RequestMethod.GET)
	public ResponseEntity<?> getSuggestedFriends(HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		List<UserDetails> list=friendsDAO.getSuggestedFriends(userName);
		return new ResponseEntity<List<UserDetails>>(list,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/sendFriendRequest/{toId}",method=RequestMethod.GET)
	public ResponseEntity<?> sendFriendRequest(@PathVariable String toId,HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		try{
			Friends friend=friendsDAO.getFriend(toId, userName);
			if(friend==null){
				friend=new Friends();
				friend.setToID(toId);
				friend.setFromID(userName);
			}
			friend.setStatus('P');
			friendsDAO.addOrUpdateFriend(friend);
			return new ResponseEntity<Void>(HttpStatus.OK);
	    }
		catch(Exception e){
				Error error=new Error(1,"Unable to send friend request!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);	
		}			
		
	}
	
	@RequestMapping(value="/getFriendsList",method=RequestMethod.GET)
	public ResponseEntity<?> getFriendsList(HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		List<UserDetails> list=friendsDAO.getFriendsList(userName);
		return new ResponseEntity<List<UserDetails>>(list,HttpStatus.OK);	
	}

	@RequestMapping(value="/getFriendRequests",method=RequestMethod.GET)
	public ResponseEntity<?> getFriendRequests(HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		List<UserDetails> list=friendsDAO.getFriendRequests(userName);
		return new ResponseEntity<List<UserDetails>>(list,HttpStatus.OK);	
	}

	@RequestMapping(value="/getSentRequests",method=RequestMethod.GET)
	public ResponseEntity<?> getSentRequests(HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		List<UserDetails> list=friendsDAO.getSentRequests(userName);
		return new ResponseEntity<List<UserDetails>>(list,HttpStatus.OK);	
	}

	@RequestMapping(value="/updateFriendStatus/{id}/{status}",method=RequestMethod.GET)
	public ResponseEntity<?> updateFriendStatus(@PathVariable String id,@PathVariable char status,HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		try{
		Friends friend=friendsDAO.getFriend(userName,id);
		if(friend==null||friend.getStatus()=='R')
			friend=friendsDAO.getFriend(id,userName);
		friend.setStatus(status);
		friendsDAO.addOrUpdateFriend(friend);
		return new ResponseEntity<Void>(HttpStatus.OK);
    	}
		catch(Exception e){
			System.out.print(e);
			Error error=new Error(1,"Unable to update friend!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);	
		}			
	}
	
	@RequestMapping(value="/getFriend/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> getFriend(@PathVariable String id,HttpSession session){
		String userName=(String)session.getAttribute("userName");
		if(userName==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		Friends friend=friendsDAO.getFriend(userName,id);
		if(friend==null||friend.getStatus()=='R')
			friend=friendsDAO.getFriend(id,userName);
		return new ResponseEntity<Friends>(friend,HttpStatus.OK);
    }

}
