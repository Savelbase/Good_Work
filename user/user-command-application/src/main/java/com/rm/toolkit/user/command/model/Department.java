package com.rm.toolkit.user.command.model;

import com.rm.toolkit.user.command.model.iface.Versionable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "cx_department_name"))
@SQLDelete(sql = "UPDATE department SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class Department implements Versionable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String headId;

    private Integer version;

    @Column(nullable = false)
    private boolean deletable = true;

    @Column(nullable = false)
    private boolean deleted = false;
}
