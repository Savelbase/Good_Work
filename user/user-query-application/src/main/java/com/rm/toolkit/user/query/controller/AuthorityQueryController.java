package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.security.AuthorityType;
import com.rm.toolkit.user.query.service.AuthorityQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Получение информации о правах ролей")
@RestController
@RequestMapping("/api/v1/authorities")
@RequiredArgsConstructor
@Slf4j
public class AuthorityQueryController {

    private final AuthorityQueryService authorityQueryService;

    @Operation(summary = "Получение списка прав, которыми могут обладать роли")
    @GetMapping
    public AuthorityType[] getAllAuthorities() {
        log.info("Вызван GET /api/v1/authorities");

        return AuthorityType.values();
    }
}
