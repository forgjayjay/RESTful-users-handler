package com.assignment.test;

import java.io.ByteArrayOutputStream;

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

    @Autowired
    UserValidator validator;
    //get put post delete

    @PostMapping("/createUser")
    public ResponseEntity<String> postUser(@RequestBody String str){
        try{
            User user = mapper.readValue(str, User.class);
            if(!validator.validate(user).isEmpty()) return ResponseEntity.badRequest().body("Some of the required fields are not present/are incorrect");
            
            if(userHandler.add(user)){
                return ResponseEntity.ok(user.toString());
            }
        } catch (Exception e) {
            ResponseEntity.internalServerError().body("Something went wrong");
        }    
        return ResponseEntity.badRequest().body("User may already exist or is underaged");
    }

    @DeleteMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestBody String str){
        try {
            User user =  mapper.readValue(str, User.class); 
            if(userHandler.remove(user)){
                return ResponseEntity.ok().body("Removed given user");
            }
        } catch (Exception e) {
            ResponseEntity.internalServerError().body("Something went wrong");
        }        
        return ResponseEntity.badRequest().body("User may not exist");
    }
    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<String> removeUserById(@PathVariable int id){
           
        if(userHandler.removeById(id)){
            return ResponseEntity.ok().body("Removed given user");
        }
        return ResponseEntity.badRequest().body("User may not exist");
    }

    @PutMapping("path/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
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
