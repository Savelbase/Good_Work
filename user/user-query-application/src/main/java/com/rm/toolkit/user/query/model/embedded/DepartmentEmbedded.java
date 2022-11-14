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
public class DepartmentEmbedded {

    @Schema(example = "62f69579-ad7f-4dfd-9fea-b718d7c0d968")
    protected String id;

    @Schema(example = "Dev")
    protected String name;
}
