package com.assignment.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
public class RESTController {
    @Autowired 
    private UserHandler userHandler;

    @Autowired
    private Gson gson;

    @Autowired
    UserValidator validator;
    //get put post delete

    @PostMapping("/createUser")
    public ResponseEntity<String> postUser(@Valid @RequestBody String str){
        System.out.println(str);
        User user = gson.fromJson(str, User.class);
        //User user = new User(str, str, str, str, str, 0);
        if(!validator.validate(user).isEmpty()) return ResponseEntity.badRequest().body("Some of the required fields are not present/are incorrect");
        userHandler.add(user);
        
        System.out.println(user.toString());
        return ResponseEntity.ok(user.toString());
    }

    @GetMapping("/getAllUsers")
    public String getUsers() {
        return gson.toJson(userHandler.getAllUsers());
    }
    
}
