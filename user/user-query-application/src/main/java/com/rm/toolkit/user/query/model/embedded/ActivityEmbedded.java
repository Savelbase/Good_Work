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
public class ActivityEmbedded {

    @Schema(example = "99e2f6b1-d90b-4d23-a03c-63d878298757")
    protected String id;

    @Schema(example = "Оценка проектов")
    protected String name;
}
