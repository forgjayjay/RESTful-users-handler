package com.assignment.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {
    
    Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    UserValidator validator;

    private List<User> userList = new ArrayList<>();
    private Map<Integer, User> userMap = new HashMap<>();
  

    public User add(User user){
        if(!userList.contains(user)){
            boolean valid = (validator.validate(user).isEmpty()) && (validator.validateAge(user));
            if(!valid) return null;
            userList.add(user);
            user.setId(userList.indexOf(user));
            userMap.put(user.getId(), user);
            return user;
        }
        return null;
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

    public User updateUser(int index, User updatedUser){
        User user = userMap.get(index);
        User tempUser = user;
        if(user != null){
            Field[] fields = user.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals("id")) continue;
                Object userValue;
                Object updatedValue;
                try{
                    userValue = field.get(user);
                    updatedValue = field.get(updatedUser);

                    if(updatedValue==null || updatedValue == (Object)0L) continue;
                } 
                catch(NullPointerException | IllegalArgumentException | IllegalAccessException e){
                    logger.error("Expected exception at user update: "+e.getMessage());
                    continue;
                }
                if(!updatedValue.equals(userValue)){
                    try {
                        field.set(user, updatedValue);
                    } catch (Exception e) {
                        logger.error("Error during user update: ", e.getMessage());
                        return null;
                    }
                 }
            }
            if(!validator.validate(user).isEmpty() || !validator.validateAge(user)){
                logger.info("Rolling back user changes due to constraint violations");
                user = tempUser;
                return null;
            }
            return user;
        }
        return null;
    }

    public List<User> getAllUsersFromTo(Date from, Date to){
        if(!from.before(to)) return null;
        
        List<User> returnList = new ArrayList<>();
        
        for (User user : userList) {
            if(user.getBirth_date().before(to) && user.getBirth_date().after(from)){
                returnList.add(user);
            }
        }
        
        return returnList;
    }


    public User getUser(int index){
        return userList.get(index);
    }    

    public List<User> getAllUsers(){
        return userList;
    }
}
