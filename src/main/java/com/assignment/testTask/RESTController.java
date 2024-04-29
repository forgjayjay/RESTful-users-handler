package com.assignment.testTask;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.assignment.testTask.exception.ApiRequestException;
import com.assignment.testTask.exception.InternalApiException;
import com.assignment.testTask.user.User;
import com.assignment.testTask.user.UserHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class RESTController {
    @Autowired 
    private UserHandler userHandler;

    @Autowired
    private ObjectMapper mapper;

    Logger logger = LoggerFactory.getLogger(RESTController.class);

    @PostMapping("/postUser")
    public ResponseEntity<?> postUser(@RequestBody String json){
        try{
            User user = mapper.readValue(json, User.class);
            if(userHandler.add(user) != null){
                return ResponseEntity.created(new URI("/api/getUser/"+user.getId())).body("{\"data\":"+mapper.writeValueAsString(user)+"}");
            }
        } catch (JsonProcessingException | URISyntaxException e) {
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");
        }    
        throw new ApiRequestException("User already exists or constraint violation");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody String json){
        try {
            User user =  mapper.readValue(json, User.class); 
            if(userHandler.remove(user)){
                return ResponseEntity.ok().body(null);
            } else {
                throw new ApiRequestException("User doesn't exist");
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");
        }        
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable int id){
        if(userHandler.removeById(id) != null){
            return ResponseEntity.ok().body(null);
        } else {
            throw new ApiRequestException("User with id = " + id + " doesn't exist");
        }
    }

    @PutMapping("putUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody String json) {
        try{
            User user = mapper.readValue(json, User.class);
            User updatedUser = userHandler.updateUser(id, user);
            if(updatedUser != null){
                return ResponseEntity.ok().body("{\"data:\""+mapper.writeValueAsString(updatedUser)+"}");
            } else {
                throw new ApiRequestException("User with id = " + id + " doesn't exist or update contains constraint violations");
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");

        }   
    }

    @GetMapping("/getUsers/{id}")
    public ResponseEntity<?> getUsersById(@PathVariable int id) {
        User user = userHandler.getUser(id);
        if(user == null) throw new ApiRequestException("User with id = " + id + " doesn't exist");
        try{
            return  ResponseEntity.ok().body("{\"data:\""+mapper.writeValueAsString(user)+"}");
        } catch (JsonProcessingException e){
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");
        }
    }
    
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers() {
        try{
            Collection<?> userList = userHandler.getAllUsers();
            return  ResponseEntity.ok().body("{\"data\":"+mapper.writeValueAsString(userList)+"}");
        } catch (JsonProcessingException e){
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");
        }
    }
    
    @GetMapping("/getUsersByDate")
    public ResponseEntity<?> getUsersDateToDate(
        @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
        @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
    ){
        try{
            List<User> userList = userHandler.getAllUsersFromTo(fromDate, toDate);
            if(userList == null) throw new ApiRequestException("Invalid date range");
            return ResponseEntity.ok().body("{\"data\":"+mapper.writeValueAsString(userList)+"}");
        } catch (JsonProcessingException e){
            logger.error(e.getMessage());
            throw new InternalApiException("Internal server error");
        }
    }
}