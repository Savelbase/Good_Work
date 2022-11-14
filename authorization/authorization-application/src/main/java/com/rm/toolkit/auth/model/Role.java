package com.rm.toolkit.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.auth.model.iface.Versionable;
import com.rm.toolkit.auth.security.AuthorityType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE role SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@DynamicUpdate
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "priority"})
public class Role implements Versionable {

    @Id
    private String id;

    private Integer priority;

    @Column(columnDefinition = "string-array")
    @Type(type = "string-array")
    @Enumerated(EnumType.STRING)
    private AuthorityType[] authorities;

    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean deleted = false;
}
