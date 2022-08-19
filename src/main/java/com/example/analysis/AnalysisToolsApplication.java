package com.example.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class AnalysisToolsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalysisToolsApplication.class, args);
        log.info("starting success!!");
        AnalysisFiles analysisFiles = context.getBean(AnalysisFiles.class);
        analysisFiles.run("D:\\file\\nbs");
    }

}
