package com.TaskCollab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.TaskCollab")
 @EnableJpaRepositories("com.TaskCollab.*")
 @ComponentScan(basePackages = { "com.TaskCollab.*" })
 @EntityScan("com.TaskCollab.*") 
public class TaskCollabApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskCollabApplication.class, args);
	}

}
