package com.penguinwebstudio.logger;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;

@Configuration
@EnableMongoRepositories(mongoTemplateRef = "log")
public class LoggerDatabaseConfiguration extends AbstractMongoClientConfiguration {
	
	@Value("${spring.data.mongodb.logger.uri}")
	private String mongoUri = System.getenv().get("spring.data.mongodb.logger.uri");
	
	private SimpleMongoClientDatabaseFactory mongoFactory;
	private MongoTemplate mongoTemplate;
	
	@Bean(name = "log")
	public MongoTemplate mongoTemplate() throws Exception {
		if (this.mongoTemplate == null) {
			this.mongoFactory = (SimpleMongoClientDatabaseFactory) this.mongoDbFactory();
			this.mongoTemplate = new MongoTemplate(this.mongoFactory);
			return this.mongoTemplate;
		} else {
			return this.mongoTemplate;
		}
	}
	
	@PreDestroy
	public void close() throws Exception {
		this.mongoClient().close();
		this.mongoFactory.destroy();
	}

	@Override
	protected String getDatabaseName() {
		return "winston";
	}
	
	@Override
	protected void configureClientSettings(MongoClientSettings.Builder builder) {
		builder.applyConnectionString(new ConnectionString(mongoUri));
		builder.applyToConnectionPoolSettings(connPoolBuilder -> {
			ConnectionPoolSettings.builder().maxConnectionIdleTime(1000, TimeUnit.MILLISECONDS);
		});
	}
	
}
