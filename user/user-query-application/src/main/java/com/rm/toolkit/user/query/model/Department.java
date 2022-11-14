package com.rm.toolkit.user.query.model;

import com.rm.toolkit.user.query.model.embedded.UserEmbedded;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "department", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "cx_department_name"))
@SQLDelete(sql = "UPDATE department SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@DynamicUpdate
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Department extends DepartmentBase {

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "id", column = @Column(name = "headId")),
            @AttributeOverride(name = "firstName", column = @Column(name = "headFirstName")),
            @AttributeOverride(name = "lastName", column = @Column(name = "headLastName"))
    })
    private UserEmbedded head;
}
