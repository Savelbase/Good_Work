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
public class CountryEmbedded {

    @Schema(example = "079c234e-5c41-41c6-bdf1-87fb58b83a74")
    protected String id;

    @Schema(example = "Россия")
    protected String name;
}
