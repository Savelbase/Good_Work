package com.rm.toolkit.feedbackcommandapplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name="one_to_one")
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class OneToOne implements Versionable {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "resource_manager_id", nullable = false)
    private String resourceManagerId;

    @Column(name = "date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime dateTime;

    @Column(name = "is_over", nullable = false)
    private boolean isOver;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    private Integer version;
}
