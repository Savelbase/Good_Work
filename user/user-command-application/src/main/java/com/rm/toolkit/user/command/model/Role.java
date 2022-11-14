package com.rm.toolkit.user.command.model;

import com.rm.toolkit.user.command.model.iface.Versionable;
import com.rm.toolkit.user.command.security.AuthorityType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "cx_role_name"))
@SQLDelete(sql = "UPDATE role SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@DynamicUpdate
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name", "priority"})
public class Role implements Versionable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int priority;

    @Column(columnDefinition = "text[]")
    @Type(type = "string-array")
    @Enumerated(EnumType.STRING)
    private AuthorityType[] authorities;

    private Integer version;

    @Column(nullable = false)
    private boolean immutable = false;

    @Column(nullable = false)
    private boolean deleted = false;
}
