package com.rm.toolkit.user.query.model.embedded;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class RoleEmbedded {

    @Schema(example = "579fc993-6123-419a-ae3c-96b0b230f834")
    protected String id;

    @Schema(example = "Employee")
    protected String name;
}

