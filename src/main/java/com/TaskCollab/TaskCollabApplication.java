package com.TaskCollab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.TaskCollab")
@EntityScan("com.TaskCollab.entity")
@EnableJpaRepositories(basePackages = "com.TaskCollab.dao")
public class TaskCollabApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskCollabApplication.class, args);
	}

}
