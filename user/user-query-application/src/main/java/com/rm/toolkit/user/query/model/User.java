package com.rm.toolkit.user.query.model;

import com.rm.toolkit.user.query.model.embedded.*;
import com.rm.toolkit.user.query.model.type.StatusType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "cx_users_email"))
@SQLDelete(sql = "UPDATE users SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status != 'DELETED'")
@DynamicUpdate
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class User extends UserBase {

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

    @Schema(example = "employee3_dev@rmtm.work")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "countryId")),
            @AttributeOverride(name = "name", column = @Column(name = "countryName"))
    })
    private CountryEmbedded country;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "cityId")),
            @AttributeOverride(name = "name", column = @Column(name = "cityName"))
    })
    private CityEmbedded city;

    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Set<ActivityEmbedded> activities = new HashSet<>();
}