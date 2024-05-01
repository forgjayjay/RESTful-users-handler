package com.assignment.testTask.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class UserValidator {
    private Validator validator;

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
        return (LocalDate.now().isAfter(user.getBirth_date().plusYears(ageLimit)));
    }
}