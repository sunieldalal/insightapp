package com.slabs.insight.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

@Configuration
public class DynamoDbConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDbConfig.class);
     
    /**
     * Initializes the amazonDynamoDbClient so that we can perform CRUD operations 
     * on AWSDynamoDB - Our persistence store 
     * @return AmazonDynamoDBClient
     */
    @Bean
    public AmazonDynamoDBClient amazonDynamoDbClientService() {
       
      try {  
    	  
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(
            new AWSCredentialsProviderChain(
            	// /InstanceProfile will resolve credentials for production environment
                new InstanceProfileCredentialsProvider(),  
                // ProfileCredentialsProvider will resolve credentials for local dev environment
                new ProfileCredentialsProvider("insightapp")));
        
        
        // Set the AWS region - Default is US_EAST_1
        client.setRegion(Region.getRegion(Regions.US_WEST_2)); 
        LOGGER.info("DynamoDB Initialization Succeeded");
        return client;
      }
      catch (Exception ex){
        // If initialization of dynamoDB fails for any reason, throw runtime exception
        LOGGER.info("DynamoDB Initialization Failed: " + ex.getMessage());
        throw new RuntimeException("DynamoDB Initialization Failed", ex); 
      }      
    }
}
