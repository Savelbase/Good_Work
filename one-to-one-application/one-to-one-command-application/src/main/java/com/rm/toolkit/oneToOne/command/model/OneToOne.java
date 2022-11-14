package com.rm.toolkit.oneToOne.command.model;

import com.rm.toolkit.oneToOne.command.model.iface.Versionable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name="one_to_one_command")
public class OneToOne implements Versionable {

    @Id
    @Column
    private String id;

    @Column
    private String userId;

    @Column
    private String resourceManagerId;

    @Column
    private ZonedDateTime dateTime;

    @Column
    private String comment;

    @Column
    private boolean isOver;

    @Column
    private boolean isDeleted;

    private Integer version;

}
