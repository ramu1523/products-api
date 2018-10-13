package com.tgt.myretail.products.api.config;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
@Configuration
@Profile({"local", "dev", "stage", "prod"})
public class MongoConfig extends AbstractMongoConfiguration {
	
	private final String mongoDatabase;
	private final String mongoPassword;
	private final String mongoUsername;
	private final String url;
	
	@Autowired
	public MongoConfig(@Value("${mongo.uri}") String url, @Value("${mongo.database}") String mongoDatabase,
					   @Value("${mongo.datasource.username}") String mongoUsername,
					   @Value("${mongo.datasource.password}") String mongoPassword) throws UnsupportedEncodingException {
		this.url = url;
		this.mongoDatabase = mongoDatabase;
		this.mongoUsername = mongoUsername;
		this.mongoPassword = URLEncoder.encode(mongoPassword, UTF_8);
	}
	
	@Override
	protected String getDatabaseName() {
		return mongoDatabase;
	}
	
	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), getDatabaseName());
	}
	
	@Override
	public MongoClient mongoClient() {
		String mongoURL = "mongodb://" + mongoUsername + ":" + mongoPassword + "@" + url;
		MongoClientURI uri = new MongoClientURI(mongoURL);
		return new MongoClient(uri);
	}
	
}
