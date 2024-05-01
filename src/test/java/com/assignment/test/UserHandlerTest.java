package com.assignment.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.testTask.TestTaskApplication;
import com.assignment.testTask.user.User;
import com.assignment.testTask.user.UserHandler;

@SpringBootTest(classes = TestTaskApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserHandlerTest {

    @Autowired
    UserHandler userHandler;

    public List<User> userList;

    @BeforeAll
    public void testSetup() throws ParseException {
        userList = List.of(
            new User(LocalDate.of(1994, 4,5), null, "Ivan", "Schmidt", "Hofstrasse 4, Hilden", "4915510686794"),                            //missing email
            new User(LocalDate.of(2007, 6, 30), "IvanSchmidt@example.com", "Ivan", null, "Hofstrasse 4, Hilden", "4915510686794"),            //missing last name and under the age requirement 
            new User(LocalDate.of(1998, 8,13), "IgorFalaev@example.c", "Igor", "Falaev", "Wolfgang Str. 35c, New York", "1-212-456-7890"),   //incorrect email format
            new User(LocalDate.of(2005, 9, 24), "manuel-rodrigues@example.com", "Manuel", "Rodrigues", null, "746 189 715"),   
            new User(LocalDate.of(2001, 05, 24), "ethat2004@example.com", "Ehan", "Regor", "Turkey", "135031050136")                           //everything is correct
        );
        assertThat(userList).isNotEmpty();
    }

    @AfterEach
    public void testCleanup(){
        userHandler.clear();
    }

    @Test
    public void addTest() {
        List<Boolean> addedList = new ArrayList<>();
        for (User user : userList) {
            addedList.add(userHandler.add(user) != null);
        }

        assertThat(addedList).containsExactly(false, false, false, true, true);
    }

    @Test
    public void removeByIdTest() {
        User user = userHandler.add(userList.get(3));

        User removedUser = userHandler.removeById(0);

        assertThat(user).isEqualTo(removedUser);
    }

    @Test
    public void removeTest() {
        User user = userHandler.add(userList.get(3));

        User removedUser = userHandler.remove(user);

        assertThat(user).isEqualTo(removedUser);
    }

    @Test
    public void getByIdTest() {
        User user = userHandler.add(userList.get(3));

        User removedUser = userHandler.getUser(0);

        assertThat(user).isEqualTo(removedUser);
    }

    @Test
    public void getTest() {
        User user = userHandler.add(userList.get(3));

        User removedUser = userHandler.getUser(user);

        assertThat(user).isEqualTo(removedUser);
    }

    @Test
    public void getFromToTest() throws ParseException {
        User userIncludedInRange = userHandler.add(userList.get(3));
        User userNotIncludedInRange = userHandler.add(userList.get(4));
        List<User> list = userHandler.getAllUsersFromTo(userIncludedInRange.getBirth_date(), LocalDate.now());

        assertThat(list).contains(userIncludedInRange).doesNotContain(userNotIncludedInRange);
    }

    @Test
    public void updateTest() {
        userHandler.add(userList.get(3));

        User tempUser = new User(LocalDate.of(2005, 9, 24), "manuel-rodrigues@example.com", "Manuel", "Rodrigues", null, "746 189 715");
        tempUser.setId(0);
        tempUser.setEmail("temp-mail@gmail.co");
        tempUser.setPhone("1340013513");
        User updatedUser = userHandler.updateUser(0, tempUser);

        assertEquals(tempUser, updatedUser);
    }
}
