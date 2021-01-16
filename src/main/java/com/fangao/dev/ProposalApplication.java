package com.fangao.dev;

import com.fangao.dev.common.spring.SpringHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.fangao.dev.config",
        "com.fangao.dev.*.controller",
        "com.fangao.dev.*.service",
        "com.fangao.dev.core.web"})
public class ProposalApplication {
    private static final Logger logger = LoggerFactory.getLogger(ProposalApplication.class);
	public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ProposalApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        SpringHelper.setApplicationContext(application.run(args));
        if (logger.isInfoEnabled()) {
            System.out.println("入口：http://localhost:8088");
            System.out.println("文档：http://localhost:8088/swagger-ui.html");
        }
	}

}
