package com.assignment.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.testTask.MainController;
import com.assignment.testTask.TestTaskApplication;


@SpringBootTest(classes = TestTaskApplication.class)
class SmokeTest {

	@Autowired
	private MainController controller;

	@Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
}