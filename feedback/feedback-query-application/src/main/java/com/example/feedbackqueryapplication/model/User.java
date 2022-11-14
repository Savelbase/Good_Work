package com.example.feedbackqueryapplication.model;

import com.example.feedbackqueryapplication.model.type.Role;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer version;
}
