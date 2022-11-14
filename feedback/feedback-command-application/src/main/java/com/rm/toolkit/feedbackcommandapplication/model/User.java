package com.rm.toolkit.feedbackcommandapplication.model;

import com.rm.toolkit.feedbackcommandapplication.model.type.Versionable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User implements Versionable {

    @Id
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "resource_manager_id", nullable = false)
    private String resourceManagerId;

    @Column(name = "department_id", nullable = false)
    private String departmentId;

    private Integer version;
}
