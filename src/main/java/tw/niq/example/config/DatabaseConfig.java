package tw.niq.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;

@EnableR2dbcAuditing
@Configuration
public class DatabaseConfig {
	
	@Value("classpath:/schema.sql")
	Resource resource;

	@Bean
	ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory) {
		
		ConnectionFactoryInitializer connectionFactoryInitializer = new ConnectionFactoryInitializer();
		
		connectionFactoryInitializer.setConnectionFactory(connectionFactory);
		
		connectionFactoryInitializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));
		
		return connectionFactoryInitializer;
	}
	
}
