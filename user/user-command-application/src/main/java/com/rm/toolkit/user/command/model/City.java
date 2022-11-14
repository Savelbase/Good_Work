package com.rm.toolkit.user.command.model;

import com.rm.toolkit.user.command.model.iface.Versionable;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@SQLDelete(sql = "UPDATE city SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@ToString(of = {"id"})
public class City implements Versionable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String countryId;

    private Integer version;

    @Column(nullable = false)
    private boolean deleted = false;
}