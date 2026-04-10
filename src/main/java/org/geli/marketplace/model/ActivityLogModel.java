package org.geli.marketplace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activity_logs")
public class ActivityLogModel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "status")
    private String status;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "request_data", columnDefinition = "TEXT")
    private String requestData;

    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;
}
