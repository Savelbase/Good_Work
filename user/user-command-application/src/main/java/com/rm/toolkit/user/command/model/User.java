package com.rm.toolkit.user.command.model;

import com.rm.toolkit.user.command.model.iface.Versionable;
import com.rm.toolkit.user.command.model.type.StatusType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "cx_users_email"))
@SQLDelete(sql = "UPDATE users SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status != 'DELETED'")
@DynamicUpdate
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "firstName", "lastName", "email"})
public class User implements Versionable {

    @Id
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String resourceManagerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.ACTIVE;

    private String departmentId;

    private String roleId;

    private Integer version;
}
