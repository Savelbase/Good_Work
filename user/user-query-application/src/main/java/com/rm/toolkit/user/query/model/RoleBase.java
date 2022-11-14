package com.rm.toolkit.user.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.user.query.model.iface.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name"})
@SuperBuilder
public abstract class RoleBase implements Versionable {

    @Schema(example = "579fc993-6123-419a-ae3c-96b0b230f834")
    @Id
    protected String id;

    @Schema(example = "Employee")
    @Column(nullable = false)
    @NotBlank
    protected String name;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean deleted = false;
}
