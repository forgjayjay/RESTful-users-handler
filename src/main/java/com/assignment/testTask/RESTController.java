package com.assignment.testTask;

import java.net.URI;
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
    public ResponseEntity<String> postUser(@RequestBody String json){
        try{
            User user = mapper.readValue(json, User.class);
            if(userHandler.add(user) != null){
                return ResponseEntity.created(new URI("/api/getUser/"+user.getId())).body("{\"data\":"+mapper.writeValueAsString(user)+"}");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }    
        return ResponseEntity.badRequest().body("{\"error\":\"User exists or constraint violation\"}");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody String json){
        try {
            User user =  mapper.readValue(json, User.class); 
            if(userHandler.remove(user)){
                return ResponseEntity.ok().body(null);
            } else {
                return ResponseEntity.badRequest().body("{\"error\":\"User may not exist\"}");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }        
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
        if(userHandler.removeById(id)){
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().body("{\"error\":\"User may not exist\"}");
        }
    }

    @PutMapping("putUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody String json) {
        try{
            User user = mapper.readValue(json, User.class);
            User updatedUser = userHandler.updateUser(id, user);
            if(updatedUser != null){
                return ResponseEntity.ok().body("{\"data:\""+mapper.writeValueAsString(updatedUser)+"}");
            } else {
                return ResponseEntity.badRequest().body("{\"error\":\"User may not exist or constraint violation\"}");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }   
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<String> getMethodName(@PathVariable int id) {
        try{
            User user = userHandler.getUser(id);
            if(user == null) return ResponseEntity.badRequest().body("{\"error\":\"User may not exist\"}");
            return  ResponseEntity.ok().body("{\"data:\""+mapper.writeValueAsString(user)+"}");
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }
    }
    
    @GetMapping("/getAllUsers")
    public ResponseEntity<String> getUsers() {
        try{
            List<User> userList = userHandler.getAllUsers();
            return  ResponseEntity.ok().body("{\"data\":"+mapper.writeValueAsString(userList)+"}");
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }
    }
    
    @GetMapping("/getUsersByDate")
    public ResponseEntity<String> getUsersDateToDate(
        @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
        @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
    ){
        try{
            List<User> userList = userHandler.getAllUsersFromTo(fromDate, toDate);
            if(userList == null) return ResponseEntity.badRequest().body("{\"error\":\"User may not exist or constraint violation\"}");
            return ResponseEntity.ok().body("{\"data\":"+mapper.writeValueAsString(userList)+"}");
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("{\"error\":\"Internal server error\"}");
        }
    }
}