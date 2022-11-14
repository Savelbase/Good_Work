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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "uk_city_name"))
@SQLDelete(sql = "UPDATE city SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class City implements Versionable {

    @Schema(example = "cf8a4b92-8c2c-40bb-8403-cfa0964e7c47")
    @Id
    private String id;

    @Schema(example = "Москва")
    @Column(length = 20, nullable = false)
    @NotBlank
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @JsonIgnore
    private Integer version;

    @Column(nullable = false)
    @JsonIgnore
    protected boolean deleted = false;
}
