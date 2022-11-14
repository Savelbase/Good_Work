package com.rm.toolkit.oneToOne.command.controller;

import com.rm.toolkit.oneToOne.command.dto.request.CreateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.dto.request.UpdateOneToOneRequest;
import com.rm.toolkit.oneToOne.command.dto.response.SuccessResponse;
import com.rm.toolkit.oneToOne.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.oneToOne.command.dto.response.error.ConflictErrorResponse;
import com.rm.toolkit.oneToOne.command.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.oneToOne.command.security.SecurityUtil;
import com.rm.toolkit.oneToOne.command.service.OneToOneCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/121")
@RequiredArgsConstructor
@Tag(name = "Работа с one-to-one")
public class OneToOneCommandController {

    private final OneToOneCommandService oneToOneCommandService;

    @Operation(summary = "Создать one-to-one с сотрудником")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "one-to-one с сотрудником успешно создан",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"cf8a4b92-8c2c-40bb-8403-cfa0964e7c47\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле comment",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: ccccooooooommmmmmmmmmmmmeeeeeennntttt\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"USER_NOT_FOUND\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "412", description = "Пользователь с таким id отсутствует в пуле подчиненных",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2022-01-20T13:32:12.408Z\",\n" +
                                    "  \"errorType\": \"USER_IN_POOL_IS_ABSENT\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "409", description = "121 встреча не может быть назначена на прошедшую дату", content =
            @Content(schema = @Schema(implementation = ConflictErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.command.security.AuthorityType).ONE_TO_ONE)")
    @PostMapping
    public SuccessResponse createOneToOne(@Valid @RequestBody CreateOneToOneRequest request, Principal principal) {
        log.info("Пользователь " + principal.getName() + " вызывает /create endpoint");

        String oneToOneId = oneToOneCommandService.createOneToOne(request, SecurityUtil.getCurrentUserId());

        log.info("Пользователь " + principal.getName() + " успешно назначил 121 на:"
                + request.getDateTime());

        return new SuccessResponse(oneToOneId);
    }

    @Operation(summary = "Изменить статус встречи на \"завершено\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус встречи успешно изменен на \"завершено\"",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Встреча с таким id не найдена",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "one-to-one не может быть завершена раньше " +
                    "назначенной даты", content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.command.security.AuthorityType).ONE_TO_ONE)")
    @PatchMapping("/complete/{oneToOneId}")
    public SuccessResponse completeOneToOne(@PathVariable String oneToOneId, Principal principal) {
        log.info("Пользователь " + principal.getName() +
                " отправил запрос на завершение встречи с id: " + oneToOneId);

        oneToOneCommandService.completeOneToOne(oneToOneId, SecurityUtil.getCurrentUserId());

        log.info("Встреча с id: " + oneToOneId + " успешно завершена");
        return new SuccessResponse("One-to-one с id: " + oneToOneId + " успешно завершён");
    }

    @Operation(summary = "Обновить one-to-one с сотрудником")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "one-to-one с сотрудником успешно обновлён",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Слишком длинное поле comment",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                                    "  \"errorType\": \"Field too long: ccccooooooommmmmmmmmmmmmeeeeeennntttt\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "one-to-one для пользователя с таким  " +
                    "id не найден", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "121 встреча не может быть назначена на прошедшую дату или изменена прошедшая встреча",
                    content = @Content(schema = @Schema(implementation = ConflictErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.command.security.AuthorityType).ONE_TO_ONE)")
    @PatchMapping("/{oneToOneId}")
    public SuccessResponse updateOneToOne(@PathVariable String oneToOneId,
                                          @Valid @RequestBody UpdateOneToOneRequest request, Principal principal) {
        log.info("Пользователь " + principal.getName() + " обновляет пользователя: " + oneToOneId);

        oneToOneCommandService.updateOneToOne(oneToOneId, request, SecurityUtil.getCurrentUserId());

        log.info("Пользователь " + principal.getName() + " успешно обновил 121 на:"
                + request.getDateTime());

        return new SuccessResponse("One-to-one с id: " + oneToOneId +
                " успешно обновлён и назначен на: " + request.getDateTime());
    }

    @Operation(summary = "Удалить one-to-one с сотрудником")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "one-to-one с сотрудником успешно удалён",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "one-to-one для пользователя с таким  " +
                    "id не найден", content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.oneToOne.command.security.AuthorityType).ONE_TO_ONE)")
    @DeleteMapping("/{oneToOneId}")
    public SuccessResponse deleteOneToOne(@PathVariable String oneToOneId, Principal principal) {
        log.info("Пользователь " + principal.getName() + " удаляет 121 с id: " + oneToOneId);

        oneToOneCommandService.deleteOneToOne(oneToOneId, SecurityUtil.getCurrentUserId());

        log.info("Пользователь " + principal.getName() + "успешно удалил 121 с id:" + oneToOneId);

        return new SuccessResponse("One-to-one с id: " + oneToOneId + " успешно удалён");
    }
}
