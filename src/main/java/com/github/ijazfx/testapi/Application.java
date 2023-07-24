package com.github.ijazfx.testapi;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.util.TRCalendarUtil;

@SpringBootApplication
public class Application {

	@Autowired
	GrapheneeCoreConfiguration graphenee;

	public static void main(String[] args) {
		TRCalendarUtil.setCustomDateFormatter(new SimpleDateFormat("dd.MM.yyyy"));
		TRCalendarUtil.setCustomDateTimeFormatter(new SimpleDateFormat("dd.MM.yyyy hh:mm a"));
		TRCalendarUtil.setCustomTimeFormatter(new SimpleDateFormat("hh:mm a"));

		SpringApplication.run(Application.class, args);
	}

}
