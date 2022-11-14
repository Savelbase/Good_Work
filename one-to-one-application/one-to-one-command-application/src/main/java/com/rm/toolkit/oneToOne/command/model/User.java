package com.rm.toolkit.oneToOne.command.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Column(name = "resource_manager_id")
    private String resourceManagerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;
}
