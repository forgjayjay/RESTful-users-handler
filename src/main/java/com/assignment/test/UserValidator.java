package com.assignment.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Date;
import java.util.Set;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class UserValidator {
    private Validator validator;

    //private Logger logger = LoggerFactory.getLogger(UserValidator.class);

    @Value("${assignment.age.restriction}")
    private long ageLimit;

    public UserValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Set<ConstraintViolation<User>> validate(User user) {
        return validator.validate(user);
    }

    public boolean validateAge(User user){
        long ageLimitMillis = ageLimit * 365 * 24 * 60 * 60 * 1000;
        return (new Date().after(new Date(user.getBirth_date().getTime() + ageLimitMillis)));
    }
}
