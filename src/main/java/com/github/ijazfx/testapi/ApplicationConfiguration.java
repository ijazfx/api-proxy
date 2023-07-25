package com.github.ijazfx.testapi;

import java.io.File;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.FileStorageFactory;

@Configuration
@EnableJpaRepositories(basePackages = { "com.github.ijazfx.testapi.repo" })
@EntityScan(basePackages = { "com.github.ijazfx.testapi.model" })
@ComponentScan(basePackages = { "com.github.ijazfx.testapi" })
public class ApplicationConfiguration {

	@Bean
	FileStorage fileStorage() {
		String homeFolder = System.getProperty("user.home");
		File rootFolder = new File(homeFolder + File.separator + ".testapi");
		FileStorage storage = FileStorageFactory.createLocalFileStorage(rootFolder);
		return storage;
	}

}
