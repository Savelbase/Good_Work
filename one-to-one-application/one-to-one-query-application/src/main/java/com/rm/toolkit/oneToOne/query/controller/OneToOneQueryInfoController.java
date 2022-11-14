package com.rm.toolkit.oneToOne.query.controller;

import com.rm.toolkit.oneToOne.query.dto.response.OneToOneResponse;
import com.rm.toolkit.oneToOne.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.oneToOne.query.service.OneToOneQueryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(OneToOneQueryInfoController.REST_URL)
@RequiredArgsConstructor
@Tag(name = "Получение информации о one-to-one встречах")
public class OneToOneQueryInfoController {

    static final String REST_URL = "/api/121";
    private static final String VERSION_TWO = "/v2";
    public static final String FINISHED_USER_ID = "/finished/{userId}";
    public static final String SCHEDULED_USER_ID = "/scheduled/{userId}";
    public static final String ONE_TO_ONE_ID = "/{oneToOneId}";
    public static final String FINISHED_RM_ID = "/completed-by-rm/{rmId}";
    public static final String SCHEDULED_RM_ID = "/appointed-by-rm/{rmId}";
    private final OneToOneQueryInfoService oneToOneQueryInfoService;

    @Operation(summary = "Просмотреть список завершённых one-to-one сотрудника со своим ресурсным менеджером")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список встреч", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = OneToOneResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping(VERSION_TWO + FINISHED_USER_ID)
    public Page<OneToOneResponse> getFinishedOneToOne(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        log.info("Вызван GET {} | page={} size={} userId={}", REST_URL + VERSION_TWO + FINISHED_USER_ID, page, size, userId);

        return oneToOneQueryInfoService.getFinishedOneToOne(userId, page, size);
    }

    @Operation(summary = "Просмотреть список запланированных one-to-one сотрудника со своим ресурсным менеджером")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список встреч", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = OneToOneResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @GetMapping(VERSION_TWO + SCHEDULED_USER_ID)
    public Page<OneToOneResponse> getScheduledOneToOne(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        log.info("Вызван GET {} | page={} size={} userId={}", REST_URL + VERSION_TWO + SCHEDULED_USER_ID, page, size, userId);

        return oneToOneQueryInfoService.getScheduledOneToOne(userId, page, size);
    }

    @Operation(summary = "Просмотреть определённую one-to-one встречу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Встреча",
                    content = @Content(schema = @Schema(implementation = OneToOneResponse.class))),
            @ApiResponse(responseCode = "404", description = "one-to-one с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"ONE_TO_ONE_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).VIEW_ONE_TO_ONE)")
    @GetMapping(VERSION_TWO + ONE_TO_ONE_ID)
    public OneToOneResponse getOneToOne(@PathVariable String oneToOneId) {
        log.info("Вызван GET {}/{}", REST_URL + VERSION_TWO, oneToOneId);

        return oneToOneQueryInfoService.getOneToOne(oneToOneId);
    }

    @Operation(summary = "Просмотреть список завершённых one-to-one ресурсного менеджера со своими подчиненными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список встреч", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = OneToOneResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).VIEW_ONE_TO_ONE)")
    @GetMapping(VERSION_TWO + FINISHED_RM_ID)
    public Page<OneToOneResponse> getCompletedOneToOneByRM(
            @PathVariable String rmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        log.info("Вызван GET {} | page={} size={} rmId={}", REST_URL + VERSION_TWO + FINISHED_RM_ID, page, size, rmId);

        return oneToOneQueryInfoService.getCompletedOneToOneByRM(rmId, page, size);
    }

    @Operation(summary = "Просмотреть список назначенных one-to-one ресурсным менеджером со своими подчиненными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список встреч", content = @Content(array = @ArraySchema(
                    schema = @Schema(implementation = OneToOneResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class),
                    examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"USER_NOT_FOUND_BY_ID\"\n" +
                            "}")))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.query.security.AuthorityType).VIEW_ONE_TO_ONE)")
    @GetMapping(VERSION_TWO + SCHEDULED_RM_ID)
    public Page<OneToOneResponse> getAppointedOneToOneByRM(
            @PathVariable String rmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        log.info("Вызван GET {} | page={} size={} rmId={}", REST_URL + VERSION_TWO + SCHEDULED_RM_ID, page, size, rmId);

        return oneToOneQueryInfoService.getAppointedOneToOneByRM(rmId, page, size);
    }
}
