package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfiguration;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfiguration twitterToKafkaServiceConfiguration;

    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private TwitterStream twitterStream;

    public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfiguration configData, TwitterKafkaStatusListener listener) {
        this.twitterToKafkaServiceConfiguration = configData;
        this.twitterKafkaStatusListener = listener;
    }


    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        addFilter();
    }

    @PreDestroy
    public void shutDown() {
        if(twitterStream != null) {
            LOG.info("Closing twitter stream!");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = twitterToKafkaServiceConfiguration.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        LOG.info("Started filtering twitter stream for keywors {} ", Arrays.toString(keywords));
    }
}
