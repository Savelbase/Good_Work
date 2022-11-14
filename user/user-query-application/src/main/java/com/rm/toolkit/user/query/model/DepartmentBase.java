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
@ToString(of = {"id", "name"})
@SuperBuilder
public abstract class DepartmentBase implements Versionable {

    @Schema(example = "62f69579-ad7f-4dfd-9fea-b718d7c0d968")
    @Id
    protected String id;

    @Schema(example = "Dev")
    @Column
    protected String name;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean deleted = false;
}
