package com.assignment.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class UserValidator {
    private Validator validator;

    private Logger logger = LoggerFactory.getLogger(UserValidator.class);

    public UserValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Set<ConstraintViolation<User>> validate(User user) {
        logger.info("Validating user: " + user.toString());
        return validator.validate(user);
    }
}
