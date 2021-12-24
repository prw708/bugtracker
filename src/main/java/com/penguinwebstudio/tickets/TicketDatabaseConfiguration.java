package com.penguinwebstudio.tickets;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.ConnectionPoolSettings;

@Configuration
@EnableMongoRepositories(mongoTemplateRef = "ticket")
public class TicketDatabaseConfiguration extends AbstractMongoClientConfiguration {
	
	@Value("${spring.data.mongodb.uri}")
	private String mongoUri = System.getenv().get("spring.data.mongodb.uri");
	
	private SimpleMongoClientDatabaseFactory mongoFactory;
	private MongoTemplate mongoTemplate;

	@Bean(name = "ticket")
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
		return "web";
	}
	
	@Override
	protected void configureClientSettings(MongoClientSettings.Builder builder) {
		builder.applyConnectionString(new ConnectionString(mongoUri));
		builder.applyToConnectionPoolSettings(connPoolBuilder -> {
			ConnectionPoolSettings.builder().maxConnectionIdleTime(1000, TimeUnit.MILLISECONDS);
		});
	}
	
}
