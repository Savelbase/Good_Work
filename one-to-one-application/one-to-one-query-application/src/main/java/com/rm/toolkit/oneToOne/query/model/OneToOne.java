package com.rm.toolkit.oneToOne.query.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.oneToOne.query.model.iface.Versionable;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@DynamicUpdate
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name="one_to_one_query")
public class OneToOne implements Versionable {

    @Id
    @Column
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "resource_manager_id")
    private String resourceManagerId;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_over")
    private Boolean isOver;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @JsonIgnore
    private Integer version;
}
