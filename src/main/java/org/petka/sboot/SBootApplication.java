package org.petka.sboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SBootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}


	/**
	 * This method allows spring-boot application to be deployed on container as a war file.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return configureApplication(application);
	}

	/**
	 * This method shares configurations between spring-boot and war applications.
	 *
	 * @param application
	 * @return
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder application) {
		return application.sources(SBootApplication.class);
	}
}
