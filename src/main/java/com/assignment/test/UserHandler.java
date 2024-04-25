package com.assignment.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {
    
    private List<User> userList = new ArrayList<>();
    private Map<Integer, User> userMap = new HashMap<>();
    @Value("${assignment.age.restriction}")
    private long ageLimit;

    public boolean add(User user){
        if(!userList.contains(user)){
            long userBirthday = ageLimit * 365 * 24 * 60 * 60 * 1000;
            if(!new Date().after(new Date(user.getBirthday().getTime() + userBirthday))) return false;
            boolean added = userList.add(user);
            user.setId(userList.indexOf(user));
            userMap.put(user.getId(), user);
            return added;
        }
        return false;
    }

    public boolean remove(Object user){
        userMap.remove(userList.indexOf(user));
        return userList.remove(user);
    }
    public boolean removeById(int id){
        boolean removed = userList.remove(userMap.get(id));
        userMap.remove(id);
        return removed;
    }
    public User get(int index){
        return userList.get(index);
    }

    
    public List<User> getAllUsers(){
        return userList;
    }
}
