package botobo.core.acceptance;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.google.common.base.CaseFormat;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Profile("test")
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entityType -> Objects.nonNull(entityType.getJavaType().getAnnotation(Entity.class)))
                .map(entityType -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityType.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET foreign_key_checks = 0;").executeUpdate();
        tableNames.forEach(
                tableName -> executeQueryWithTable(tableName)
        );
        entityManager.createNativeQuery("SET foreign_key_checks = 1;").executeUpdate();
        redisConnectionFactory.getConnection().flushAll();
    }

    private void executeQueryWithTable(String tableName) {
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + ";").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1;").executeUpdate();
    }

}
