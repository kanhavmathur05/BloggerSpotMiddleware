package com.collaborationproject.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.collaborationproject.model.UserDetails;
import com.collaborationproject.service.UserDetailsService;
import com.collaborationproject.model.Error;

@RestController
public class UserController {
	@Autowired
	private UserDetailsService userDetailsService;
	/*@Autowired
	private SockController sockController;*/
	
	@RequestMapping(value="/registerUser",method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody UserDetails userDetails){
    	try{
    		System.out.print(userDetails.getUserName());
    		UserDetails duplicateUserDetails=userDetailsService.getUserDetails(userDetails.getUserName());
    	if(duplicateUserDetails!=null){
    		System.out.print(2);
    		Error error=new Error(2,"Username already exists!!");
    		return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
    	}
    	duplicateUserDetails=userDetailsService.getUserDetailsByEmail(userDetails.getEmail());
    	if(duplicateUserDetails!=null){
    		System.out.print(3);
    		Error error=new Error(3,"Email address already exists!!");
    		return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
    	}
    	System.out.print(5);
    	userDetails.setOnlineStatus(false);
    	userDetailsService.insertOrUpdateUserDetails(userDetails);
		return new ResponseEntity<UserDetails>(userDetails,HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		com.collaborationproject.model.Error error=new Error(1,"Unable to register user details!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

	@RequestMapping(value="/login",method=RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody UserDetails userDetails,HttpSession session){
    	UserDetails validUserDetails=userDetailsService.login(userDetails);
    	if(validUserDetails==null){
    		Error error=new Error(4,"Invalid username or password!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
    	validUserDetails.setOnlineStatus(true);
    	userDetailsService.insertOrUpdateUserDetails(validUserDetails);
    	session.setAttribute("username", validUserDetails.getUserName());
    	session.setAttribute("role", validUserDetails.getRole());
    	return new ResponseEntity<UserDetails>(validUserDetails,HttpStatus.OK);
    }
 
	@RequestMapping(value="/logout",method=RequestMethod.GET)
    public ResponseEntity<?> logout(HttpSession session){
    	if(session.getAttribute("userName")==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
    	String userName=(String)session.getAttribute("userName");
    	UserDetails userDetails=userDetailsService.getUserDetails(userName);
    	userDetails.setOnlineStatus(false);
    	userDetailsService.insertOrUpdateUserDetails(userDetails);
    	//sockController.onLogout(userName);
    	session.removeAttribute("userName");
    	session.invalidate();
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
	
	@RequestMapping(value="/getUser/{userName}",method=RequestMethod.GET)
    public ResponseEntity<?> getUserDetails(HttpSession session,@PathVariable String userName){
    	if(session.getAttribute("userName")==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
    	UserDetails userDetails=userDetailsService.getUserDetails(userName);
    	return new ResponseEntity<UserDetails>(userDetails,HttpStatus.OK);
	}
	@RequestMapping(value="/updateUser",method=RequestMethod.POST)
	public ResponseEntity<?> updateUser(HttpSession session,@RequestBody UserDetails userDetails){
		if(session.getAttribute("userName")==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		if(!userDetailsService.getUserDetails(userDetails.getUserName()).getEmail().equals(userDetails.getEmail()))
			if(userDetailsService.getUserDetailsByEmail(userDetails.getEmail())!=null){
				Error error=new Error(3,"Email address already exists!!");
				return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
			}
		try{
    	userDetails.setOnlineStatus(true);
    	userDetailsService.insertOrUpdateUserDetails(userDetails);
		return new ResponseEntity<UserDetails>(userDetails,HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to Update user details!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
}