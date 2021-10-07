package com.example.youtubedb.domain;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @PrePersist
  private void prePersist(){
    this.createdAt = ZonedDateTime.now();
    this.updatedAt = ZonedDateTime.now();
  }

  @PreUpdate
  private void preUpdate(){
    this.updatedAt = ZonedDateTime.now();
  }
}
