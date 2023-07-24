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
@EnableJpaRepositories(basePackages = { "com.github.ijazfx.urlproxy.repo" })
@EntityScan(basePackages = { "com.github.ijazfx.urlproxy.model" })
@ComponentScan(basePackages = { "com.github.ijazfx.urlproxy" })
public class ApplicationConfiguration {

	@Bean
	FileStorage fileStorage() {
		String homeFolder = System.getProperty("user.home");
		File rootFolder = new File(homeFolder + File.separator + ".urlproxy");
		FileStorage storage = FileStorageFactory.createLocalFileStorage(rootFolder);
		return storage;
	}

}
