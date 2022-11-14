package com.rm.toolkit.user.query.model.embedded;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class UserEmbedded {

    @Schema(example = "3515fcc1-477d-4b86-a269-8be11125e13b")
    protected String id;

    @Schema(example = "Геннадий")
    @Column
    protected String firstName;

    @Schema(example = "Джавайло")
    @Column
    protected String lastName;
}
