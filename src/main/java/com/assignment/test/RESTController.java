package com.assignment.test;

import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    //get put post delete

    Logger logger = LoggerFactory.getLogger(RESTController.class);

    @PostMapping("/createUser")
    public ResponseEntity<String> postUser(@RequestBody String json){
        try{
            User user = mapper.readValue(json, User.class);
            if(userHandler.add(user)){
                return ResponseEntity.ok(user.toString());
            }
        } catch (Exception e) {
            ResponseEntity.internalServerError().body("Something went wrong");
        }    
        return ResponseEntity.badRequest().body("User may already exist or is underaged");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody String json){
        try {
            User user =  mapper.readValue(json, User.class); 
            if(userHandler.remove(user)){
                return ResponseEntity.ok().body("Removed given user");
            }
        } catch (Exception e) {
            ResponseEntity.internalServerError().body("Something went wrong");
        }        
        return ResponseEntity.badRequest().body("User may not exist");
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
           
        if(userHandler.removeById(id)){
            return ResponseEntity.ok().body("Removed given user");
        }
        return ResponseEntity.badRequest().body("User may not exist");
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody String json) {
        try{
            User user = mapper.readValue(json, User.class);

            if(userHandler.updateUser(id, user)){
                return ResponseEntity.ok().body("Updated given user");
            }
        } catch (Exception e) {
            ResponseEntity.internalServerError().body("Something went wrong");
        }   
        return ResponseEntity.badRequest().body("User may not exist");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<String> getUsers() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            mapper.writeValue(out, userHandler.getAllUsers());
            byte[] data = out.toByteArray();
            return  ResponseEntity.ok().body(new String(data));
        } catch (Exception e){
            ResponseEntity.internalServerError().body("Something went wrong");
        }
        return ResponseEntity.ok().body("[]");
    }
    
}
