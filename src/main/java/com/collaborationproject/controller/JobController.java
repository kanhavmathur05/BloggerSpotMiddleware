package com.collaborationproject.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.collaborationproject.model.Error;
import com.collaborationproject.model.ApplyForJob;
import com.collaborationproject.model.Job;
import com.collaborationproject.service.JobService;
import com.collaborationproject.service.UserDetailsService;

@RestController
public class JobController {
	@Autowired
	private JobService jobService;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@RequestMapping(value="/addJob",method=RequestMethod.POST)
	public ResponseEntity<?> addJob(@RequestBody Job job,HttpSession session){
		try{
			if(session.getAttribute("userName")==null){
	    		Error error=new Error(5,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	    	}
			else if(!session.getAttribute("role").equals("admin")){
				Error error=new Error(6,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);	
			}
			job.setPostedOn(new Date());
			jobService.insertOrUpdateJob(job);
			return new ResponseEntity<Void>(HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to add Job details!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
	
	@RequestMapping(value="/deleteJob/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteJob(@PathVariable int id,HttpSession session){
		try{
			if(session.getAttribute("username")==null){
	    		Error error=new Error(5,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	    	}
			else if(!session.getAttribute("role").equals("admin")){
				Error error=new Error(6,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);	
			}
			jobService.deleteJob(jobService.getJobById(id));
			return new ResponseEntity<Void>(HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to delete Job details!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
	
	@RequestMapping(value="/getJobById/{id}",method=RequestMethod.GET)
	public ResponseEntity<?> getJobById(@PathVariable int id){
		Job job=jobService.getJobById(id);
		return new ResponseEntity<Job>(job,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/getAllJobs",method=RequestMethod.GET)
	public ResponseEntity<?> getAllJobs(){
		List<Job> list=jobService.getJob();
		return new ResponseEntity<List<Job>>(list,HttpStatus.OK);	
	}

	@RequestMapping(value="/applyForJob/{jobId}",method=RequestMethod.GET)
	public ResponseEntity<?> applyForJob(@PathVariable int jobId ,HttpSession session){
		try{
			String userName=(String)session.getAttribute("userName");
			if(userName==null){
	    		Error error=new Error(5,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	    	}
			if(jobService.checkIfApplied(jobId, userDetailsService.getUserDetails(userName))){
	    		Error error=new Error(8,"Not allowed to apply again!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.ALREADY_REPORTED);
	    	}
			ApplyForJob applyForJob=new ApplyForJob();
			applyForJob.setAppliedFor(jobId);
			applyForJob.setAppliedBy(userDetailsService.getUserDetails(userName));
			jobService.applyForJob(applyForJob);
			return new ResponseEntity<Void>(HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to apply for Job!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

	@RequestMapping(value="/getAllAppliedUser/{jobId}",method=RequestMethod.GET)
	public ResponseEntity<?> getAllAppliedUser(@PathVariable int jobId ,HttpSession session){
		if(session.getAttribute("userName")==null){
    		Error error=new Error(5,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
    	}
		else if(!session.getAttribute("role").equals("admin")){
			Error error=new Error(6,"Unauthorized User!!");
    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);	
		}
		List<ApplyForJob> list=jobService.getAllAppliedUser(jobId);
		return new ResponseEntity<List<ApplyForJob>>(list,HttpStatus.OK);	
	}
	
	@RequestMapping(value="/checkIfApplied/{jobId}",method=RequestMethod.GET)
	public ResponseEntity<?> checkIfApplied(@PathVariable int jobId ,HttpSession session){
		try{
			String userName=(String)session.getAttribute("userName");
			if(userName==null){
	    		Error error=new Error(5,"Unauthorized User!!");
	    		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
	    	}
			return new ResponseEntity<Boolean>(jobService.checkIfApplied(jobId,userDetailsService.getUserDetails(userName)),HttpStatus.OK);
    	}catch(Exception e){
    		System.out.print(e);
    		Error error=new Error(1,"Unable to check!!");
    		return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
}
