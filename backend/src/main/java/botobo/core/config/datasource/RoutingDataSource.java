package botobo.core.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/***
 * AbstractRoutingDataSource.class: 이걸 구현하면 데이터 소스에 대한 라우팅 전략을 쓸 수 있음
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    // 추후 개선의 필요가 있지만 일단 간단하게 readonly true, false 에 따라 구분하였음
    // slave 와 master 는 어떤 데이터 소스를 사용할 지 찿을 때 사용하는 key 값이 됨
    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            return "slave";
        }
        return "master";
    }
}
