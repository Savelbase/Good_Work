package com.rm.toolkit.oneToOne.command.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompleteOneToOneRequest {

    @NotNull
    protected boolean isOver;
}
