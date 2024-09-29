package org.book.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
public class AuditModel {

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;
}
