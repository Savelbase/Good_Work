package com.rm.toolkit.user.query.model;

import com.rm.toolkit.user.query.security.AuthorityType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "cx_role_name"))
@SQLDelete(sql = "UPDATE role SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@DynamicUpdate
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Role extends RoleBase {

    @Column
    private Integer priority;

    @Column(columnDefinition = "string-array")
    @Type(type = "string-array")
    @Enumerated(EnumType.STRING)
    private AuthorityType[] authorities;
}
