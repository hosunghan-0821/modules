package module.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity for 'monitor_hist' table in Dopee database
 */


@Getter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monitor_hist")
public class MonitorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private Long monitorId;

    /**
     * 모니터링 성공 유무
     */
    @Column(name = "status")
    private Boolean status;

    /**
     * 모니터링 실행 시간
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;


}
