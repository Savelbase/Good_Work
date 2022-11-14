package com.rm.toolkit.auth.controller;

import com.rm.toolkit.auth.dto.request.WhitelistRequest;
import com.rm.toolkit.auth.dto.response.SuccessResponse;
import com.rm.toolkit.auth.dto.response.WhitelistResponse;
import com.rm.toolkit.auth.service.WhitelistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Whitelist почт, которым можно отправлять")
@RestController
@RequestMapping("/auth/whitelist")
@Slf4j
@RequiredArgsConstructor
public class WhitelistController {

    private final WhitelistService whitelistService;

    @Operation(summary = "Добавить почту в whitelist")
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.auth.security.AuthorityType).SETTINGS)")
    public SuccessResponse addMailToTheWhitelist(@Valid @RequestBody WhitelistRequest whitelistRequest) {
        log.info("Вызван POST /auth/whitelist | request = {}", whitelistRequest);

        whitelistService.saveWhitelist(whitelistRequest);

        return SuccessResponse.getGeneric();
    }

    @Operation(summary = "Получить whitelist почт")
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.auth.security.AuthorityType).ONE_TO_ONE)")
    public WhitelistResponse getWhitelist() {
        log.info("Вызван GET /auth/whitelist");

        return new WhitelistResponse(whitelistService.getEmailsFromWhitelist());
    }
}
