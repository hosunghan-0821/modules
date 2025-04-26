package module.database.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import module.database.entity.Monitor;
import module.database.entity.QMonitor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static module.database.entity.QMonitor.monitor;

@Repository
@RequiredArgsConstructor
public class MonitorRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public List<Monitor> getAllMonitor() {

        // 모니터링 사이트가 많지 않기 때문에 전체조회.. 많아봐야 10개
        return jpaQueryFactory.selectFrom(monitor).fetch();

    }

}
