package com.rm.toolkit.emailsender.command.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WhitelistResponse {

    private Set<String> whitelist;
}