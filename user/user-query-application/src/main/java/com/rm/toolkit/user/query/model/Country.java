package com.rm.toolkit.user.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rm.toolkit.user.query.model.iface.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "uk_country_name"))
@SQLDelete(sql = "UPDATE country SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class Country implements Versionable {

    @Schema(example = "079c234e-5c41-41c6-bdf1-87fb58b83a74")
    @Id
    protected String id;

    @Schema(example = "Россия")
    @Column(length = 20, nullable = false)
    @NotBlank
    protected String name;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    @Column(nullable = false)
    protected boolean deleted = false;
}
