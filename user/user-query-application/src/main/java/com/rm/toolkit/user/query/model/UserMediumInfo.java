package com.rm.toolkit.user.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.user.query.model.embedded.ActivityEmbedded;
import com.rm.toolkit.user.query.model.embedded.DepartmentEmbedded;
import com.rm.toolkit.user.query.model.embedded.RoleEmbedded;
import com.rm.toolkit.user.query.model.embedded.UserEmbedded;
import com.rm.toolkit.user.query.model.type.StatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status != 'DELETED'")
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserMediumInfo extends UserBase {

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "resourceManagerId")),
            @AttributeOverride(name = "firstName", column = @Column(name = "resourceManagerFirstName")),
            @AttributeOverride(name = "lastName", column = @Column(name = "resourceManagerLastName"))
    })
    protected UserEmbedded resourceManager;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "departmentId")),
            @AttributeOverride(name = "name", column = @Column(name = "departmentName"))
    })
    protected DepartmentEmbedded department;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "roleId")),
            @AttributeOverride(name = "name", column = @Column(name = "roleName"))
    })
    protected RoleEmbedded role;

    @JsonIgnore
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status;

    // Чтобы фильтровать по активностям, придётся их доставать
    @JsonIgnore
    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Set<ActivityEmbedded> activities = new HashSet<>();
}
