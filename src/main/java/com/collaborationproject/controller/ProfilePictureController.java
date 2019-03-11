package com.collaborationproject.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.collaborationproject.model.Error;
import com.collaborationproject.dao.ProfilePictureDAO;
import com.collaborationproject.model.ProfilePicture;

@RestController
public class ProfilePictureController {
	@Autowired
	ProfilePictureDAO profilePictureDAO;
	
	@RequestMapping(value="/addProfilePicture",method=RequestMethod.POST)
	public ResponseEntity<?> addProfilePicture(@RequestParam  MultipartFile image,HttpSession session){
		try{
			String userName=(String)session.getAttribute("userName");
			if(userName==null){
	    		Error error=new Error(5,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	    	}
			ProfilePicture profilePicture=new ProfilePicture();
			profilePicture.setUsername(userName);
			profilePicture.setImage(image.getBytes());
			profilePictureDAO.insertOrUpdateProfilePicture(profilePicture);
			return new ResponseEntity<ProfilePicture>(profilePicture,HttpStatus.OK);
    	}
		catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to upload profile picture!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

	@RequestMapping(value="/getProfilePicture/{userName}",method=RequestMethod.GET)
	public @ResponseBody byte[] getProfilePicture(@PathVariable String userName){
			ProfilePicture profilePicture=profilePictureDAO.getProfilePicture(userName);
			if(profilePicture==null)
				return null;
			return profilePicture.getImage();
    	
	}
	
}
