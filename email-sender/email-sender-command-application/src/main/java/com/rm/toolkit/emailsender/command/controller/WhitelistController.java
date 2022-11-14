package com.rm.toolkit.emailsender.command.controller;

import com.rm.toolkit.emailsender.command.dto.request.WhitelistRequest;
import com.rm.toolkit.emailsender.command.dto.response.SuccessResponse;
import com.rm.toolkit.emailsender.command.dto.response.WhitelistResponse;
import com.rm.toolkit.emailsender.command.service.WhitelistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Whitelist почт, которым можно отправлять")
@RestController
@RequestMapping("/email-sender-command/whitelist")
@Slf4j
@RequiredArgsConstructor
public class WhitelistController {

    private final WhitelistService whitelistService;

    @Operation(summary = "Добавить почту в whitelist")
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.emailsender.command.security.AuthorityType).EDIT_ROLES)")
    public SuccessResponse addMailToTheWhitelist(@Valid @RequestBody WhitelistRequest whitelistRequest) {
        log.info("Вызван POST /email-sender-command/whitelist | request = {}", whitelistRequest);

        whitelistService.saveWhitelist(whitelistRequest);

        log.info("Whitelist сохранен");

        return SuccessResponse.getGeneric();
    }

    @Operation(summary = "Получить whitelist почт")
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.emailsender.command.security.AuthorityType).AUTHORIZATION)")
    public WhitelistResponse getWhitelist() {
        log.info("Вызван GET /email-sender-command/whitelist");

        return new WhitelistResponse(whitelistService.getEmailsFromWhitelist());
    }
}