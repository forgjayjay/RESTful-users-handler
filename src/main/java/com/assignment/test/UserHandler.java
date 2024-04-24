package com.assignment.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserHandler {
    
    private List<User> userList = new ArrayList<>();

    public boolean add(User user){
        return userList.add(user);
    }

    public boolean remove(User user){
        return userList.remove(user);
    }

    public User get(int index){
        return userList.get(index);
    }

    
    public List<User> getAllUsers(){
        return userList;
    }
}
