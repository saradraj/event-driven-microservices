package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfiguration;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    private final TwitterToKafkaServiceConfiguration twitterToKafkaServiceConfiguration;

    private final StreamRunner streamRunner;

    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfiguration configData, StreamRunner runner) {
        this.twitterToKafkaServiceConfiguration = configData;
        this.streamRunner = runner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("App starts...");
        LOG.info(Arrays.toString(twitterToKafkaServiceConfiguration.getTwitterKeywords().toArray(new String[] {})));
       LOG.info(twitterToKafkaServiceConfiguration.getWelcomeMessage());
       streamRunner.start();
    }
}
