package com.assignment.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.testTask.TestTaskApplication;
import com.assignment.testTask.user.User;
import com.assignment.testTask.user.UserValidator;

@SpringBootTest(classes = TestTaskApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserValidatorTest {

    @Autowired
    UserValidator userValidator;

    public List<User> userList;

    @BeforeAll
    public void testSetup() throws ParseException {
        userList = List.of(
            new User(LocalDate.of(1994, 4,5), null, "Ivan", "Schmidt", "Hofstrasse 4, Hilden", "4915510686794"),                            //missing email
            new User(LocalDate.of(2007, 6, 30), "IvanSchmidt@example.com", "Ivan", null, "Hofstrasse 4, Hilden", "4915510686794"),            //missing last name and under the age requirement 
            new User(LocalDate.of(1998, 8,13), "IgorFalaev@example.c", "Igor", "Falaev", "Wolfgang Str. 35c, New York", "1-212-456-7890"),   //incorrect email format
            new User(LocalDate.of(2005, 9, 24), "manuel-rodrigues@example.com", "Manuel", "Rodrigues", null, "746 189 715")                   //everything is correct
        );
        assertThat(userList).isNotEmpty();
    }

    @Test
    public void validateTest() {
        List<Boolean> validationList = new ArrayList<>();
        for (User user : userList) {
            validationList.add(userValidator.validate(user).isEmpty());
        }

        assertThat(validationList).containsExactly(false, false, false, true);
    }

    @Test
    public void validateAgeTest() {
        List<Boolean> validationList = new ArrayList<>();
        for (User user : userList) {
            validationList.add(userValidator.validateAge(user));
        }

        assertThat(validationList).containsExactly(true, false, true, true);
    }
}
