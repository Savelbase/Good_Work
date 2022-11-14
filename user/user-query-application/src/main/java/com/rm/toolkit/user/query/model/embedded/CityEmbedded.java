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
public class CityEmbedded {

    @Schema(example = "cf8a4b92-8c2c-40bb-8403-cfa0964e7c47")
    protected String id;

    @Schema(example = "Москва")
    protected String name;
}
