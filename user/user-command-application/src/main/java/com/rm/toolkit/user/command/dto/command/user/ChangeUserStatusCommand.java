package com.rm.toolkit.user.command.dto.command.user;

import com.rm.toolkit.user.command.model.type.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangeUserStatusCommand {

    @NotNull
    protected StatusType status;
}
