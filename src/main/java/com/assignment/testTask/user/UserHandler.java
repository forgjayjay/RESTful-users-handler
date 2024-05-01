package com.assignment.testTask.user;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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

    private Map<Integer, User> userMap = new HashMap<>();
    private int index = 0;

    public User add(User user){
        if(!userMap.values().contains(user)){
            boolean valid = (validator.validate(user).isEmpty()) && (validator.validateAge(user));
            if(!valid) return null;
            user.setId(index);
            userMap.put(user.getId(), user);
            index++;
            return user;
        }
        return null;
    }

    public User remove(User user){
        int id = -1;
        for (User i : userMap.values()) {
            if(i.getId() == user.getId()) {
                id = user.getId();
                break;
            }
        }
        return userMap.remove(id);
    }
    
    public User removeById(int id){
        return userMap.remove(id);
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

                    if(updatedValue==null) continue;
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

    public List<User> getAllUsersFromTo(LocalDate from, LocalDate to){
        if(!from.isBefore(to) || from == null) return null;
        if(to == null) to = LocalDate.now();
        List<User> returnList = new ArrayList<>();
        
        for (User user : userMap.values()) {
            if(user.getBirth_date().isBefore(to) && user.getBirth_date().isAfter(from.minusDays(1))){
                returnList.add(user);
            }
        }
        
        return returnList;
    }


    public User getUser(int index){
        return userMap.get(index);
    }

    public User getUser(User user){
        for (User tmpUser : userMap.values()) {
            if(tmpUser.equals(user)){
                return tmpUser;
            }
        }
        return null;
    }

    public void clear(){
        userMap.clear();
        index=0;
    }

    public Collection<?> getAllUsers(){
        return userMap.values();
    }
}
