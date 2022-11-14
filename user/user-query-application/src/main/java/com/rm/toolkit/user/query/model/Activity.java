package com.rm.toolkit.user.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.user.query.model.iface.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "cx_activity_name"))
@SQLDelete(sql = "UPDATE activity SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class Activity implements Versionable {

    @Schema(example = "99e2f6b1-d90b-4d23-a03c-63d878298757")
    @Id
    private String id;

    @Schema(example = "Оценка проектов")
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean deleted = false;
}