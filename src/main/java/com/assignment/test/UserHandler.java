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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {
    
    Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    UserValidator validator;

    private List<User> userList = new ArrayList<>();
    private Map<Integer, User> userMap = new HashMap<>();
    @Value("${assignment.age.restriction}")
    private long ageLimit;

    public boolean add(User user){
        if(!userList.contains(user)){
            boolean valid = validator.validate(user).isEmpty();
            if(!valid) return false;
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

    public boolean updateUser(int index, User updatedUser){
        User user = userMap.get(index);
        if(user != null){
            logger.info(user.toString() + " : " + updatedUser.toString());
            Field[] fields = user.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getName().equals("id")) continue;
                Object userValue;
                Object updatedValue;
                try{
                    userValue = field.get(user);
                    updatedValue = field.get(updatedUser); 
                    if(userValue==null || updatedValue==null) continue;
                } catch(NullPointerException | IllegalArgumentException | IllegalAccessException e){
                    logger.info(e.getMessage());
                    continue;
                }
                if(!updatedValue.equals(userValue)){
                    try {
                        field.set(user, updatedValue);
                    } catch (Exception e) {
                        logger.info("Error during user update: ", e.getMessage());
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public List<User> getAllUsers(){
        return userList;
    }
}
