package com.rm.toolkit.user.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.user.query.model.iface.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "firstname", "lastName"})
@SuperBuilder
public abstract class UserBase implements Versionable {

    @Schema(example = "3515fcc1-477d-4b86-a269-8be11125e13b")
    @Id
    protected String id;

    @Schema(example = "Геннадий")
    @Column
    protected String firstName;

    @Schema(example = "Джавайло")
    @Column
    protected String lastName;

    @Schema(example = "null")
    @Column
    protected String avatarPath;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean isRm;
}
