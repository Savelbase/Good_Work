package com.rm.toolkit.auth.model;

import com.rm.toolkit.auth.model.iface.Versionable;
import com.rm.toolkit.auth.model.type.StatusType;
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
@ToString(of = {"id", "email"})
public class User implements Versionable {

    @Id
    private String id;

    @Column(length = 35, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer loginAttempts = 0;

    @Column
    private String roleId;

    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.ACTIVE;

    private Integer version;
}
