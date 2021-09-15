package botobo.core.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

@Profile({"dev1", "dev2", "prod1", "prod2"})
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@Configuration
public class DataSourceConfig {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    public DataSourceConfig(JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.slave")
    public DataSource slaveDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(masterDataSource());

        Map<Object, Object> dataSources = Map.of(
                "master", masterDataSource(),
                "slave", slaveDataSource()
        );

        routingDataSource.setTargetDataSources(dataSources);
        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource currentDataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings()
        );
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder entityManagerFactoryBuilder =
                new EntityManagerFactoryBuilder(hibernateJpaVendorAdapter, properties, null);

        return entityManagerFactoryBuilder
                .dataSource(currentDataSource())
                .packages("botobo.core")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
