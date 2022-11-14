package com.rm.toolkit.oneToOne.query.controller;

import com.rm.toolkit.oneToOne.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.oneToOne.query.model.OneToOne;
import com.rm.toolkit.oneToOne.query.service.OneToOneQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Deprecated
@Slf4j
@RestController
@RequestMapping("/api/121")
@RequiredArgsConstructor
@Tag(name = "Работа с 121 (Старая версия API, в дальнейшем будет убрана, используйте новую версию)")
public class OneToOneQueryController {

    private final OneToOneQueryService oneToOneQueryService;

    @Deprecated
    @Operation(summary = "Просмотреть список завершённых 121 сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "завершённые one-to-one для пользователя с таким  " +
                    "id не найдены", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).ONE_TO_ONE)")
    @GetMapping("/finished/{userId}")
    public Page<OneToOne> getFinishedOneToOne(@PathVariable String userId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "50") int size, Principal principal) {
        log.info("Вызван GET /api/121/finished/{userId} | page={} size={} userId={}", page, size, userId);

        return oneToOneQueryService.getFinishedOneToOne(userId, page, size);
    }

    @Deprecated
    @Operation(summary = "Просмотреть список запланированных 121 сотрудника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "запланированные one-to-one для пользователя с таким  " +
                    "id не найдены", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).ONE_TO_ONE)")
    @GetMapping("/scheduled/{userId}")
    public Page<OneToOne> getScheduledOneToOne(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size, Principal principal) {
        log.info("Вызван GET /api/121/scheduled/{userId} | page={} size={} userId={}", page, size, userId);

        return oneToOneQueryService.getScheduledOneToOne(userId, page, size);
    }

    @Deprecated
    @Operation(summary = "Просмотреть определённую 121 встречу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "one-to-one с таким " +
                    "id не найден", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).ONE_TO_ONE)")
    @GetMapping("/{oneToOneId}")
    public OneToOne getOneToOne(@PathVariable String oneToOneId, Principal principal) {
        log.info("Вызван GET /api/121/{oneToOneId}", oneToOneId);

        return oneToOneQueryService.getOneToOne(oneToOneId);
    }

    @Deprecated
    @Operation(summary = "Просмотреть список завершённых 121 ресурсного менеджера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "завершённые one-to-one для РМа с таким  " +
                    "id не найдены", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).ONE_TO_ONE)")
    @GetMapping("/completed-by-rm/{rmId}")
    public Page<OneToOne> getCompletedOneToOneByRM(
            @PathVariable String rmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size, Principal principal) {
        log.info("Вызван GET /api/121/completed-by-rm/{rmId} | page={} size={} rmId={}", page, size, rmId);

        return oneToOneQueryService.getCompletedOneToOneByRM(rmId, page, size);
    }

    @Deprecated
    @Operation(summary = "Просмотреть список назначенных 121 ресурсным менеджером")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "запланированные one-to-one для РМа с таким  " +
                    "id не найдены", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).ONE_TO_ONE)")
    @GetMapping("/appointed-by-rm/{rmId}")
    public Page<OneToOne> getAppointedOneToOneByRM(
            @PathVariable String rmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size, Principal principal) {
        log.info("Вызван GET /api/121/appointed-by-rm/{rmId} | page={} size={} rmId={}", page, size, rmId);

        return oneToOneQueryService.getAppointedOneToOneByRM(rmId, page, size);
    }
}
