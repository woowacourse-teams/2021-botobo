package botobo.core.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;


/*** application-dev.yml 에 이런 게 들어갈거임
 * datasource:
 *   master:
 *     driver-class-name: org.maraidb.jdbc
 *     jdbc-url: jdbc:mariadb://xxx.xxx.xxx.x:3306/botobo?userSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
 *     username: xxx
 *     password: xxx
 *   slave:
 *     driver-class-name: org.maraidb.jdbc
 *     jdbc-url: jdbc:mariadb://xxx.xxx.xxx.x:3306/botobo?userSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
 *     username: xxx
 *     password: xxx
 */

/*** 참고 링크
 * https://huisam.tistory.com/entry/routingDataSource
 * http://kwon37xi.egloos.com/5364167
 */

@Profile("dev")
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class) // 기본 AutoConfiguration 하지 않도록
@Configuration
public class DataSourceConfig {

    // write 용 master 데이터소스 등록
    @Bean
    @ConfigurationProperties(prefix = "datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    // read 용 slave 데이터소스 등록
    @Bean
    @ConfigurationProperties(prefix = "datasource.slave")
    public DataSource slaveDataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
        // 로직의 실수가 있어서 insert 인데 slave DB로 갔다고 해도 에러 터지도록
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        // 기본 데이터 베이스
        routingDataSource.setDefaultTargetDataSource(masterDataSource());

        // 여기서 키-데이터소스 설정을 하고 RoutingDataSource 에 주입함
        Map<Object, Object> dataSources = Map.of(
                "master", masterDataSource(),
                "slave", slaveDataSource()
        );

        routingDataSource.setTargetDataSources(dataSources);
        return routingDataSource;
    }

    /*
    스프링은 @Transactional을 만나면 다음 순서로 일을 처리한다.
    TransactionManager 선별 -> DataSource에서 Connection 획득 -> Transaction 동기화(Synchronization)
    라우팅을 사용한다면 동기화를 마친 다음에 커넥션을 획득해야 올바른 동작인데 그렇지 못하다.
    LazyConnectionDataSourceProxy 는 사용하는 시점에 Connection 을 얻게 해주는 클래스이다!
    잘 이해 안 가는데 일단 요약해서 적어둠.
    자세한 내용이 궁금하면 2번째 링크 참고
     */
    @Primary
    @Bean
    public DataSource currentDataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }


    // 아래는 jpa 관련한 빈 등록하는 건데, AutoConfiguration 을 안 하게 설정해둬서 직접 등록해야 하는듯
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(currentDataSource());
        entityManagerFactoryBean.setPackagesToScan("botobo.core");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
