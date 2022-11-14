package com.rm.toolkit.comment.command.controller;

import com.rm.toolkit.comment.command.dto.command.CreateCommentCommand;
import com.rm.toolkit.comment.command.dto.command.EditCommentCommand;
import com.rm.toolkit.comment.command.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.comment.command.dto.response.success.SuccessResponse;
import com.rm.toolkit.comment.command.exception.notfound.CommentNotFoundException;
import com.rm.toolkit.comment.command.security.SecurityUtil;
import com.rm.toolkit.comment.command.service.CommentCommandService;
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

@Tag(name = "Создание комментариев , изменение и удаление")
@RestController
@RequestMapping("api/v1/comments/")
@RequiredArgsConstructor
@Slf4j
public class CommentCommandController {

    private final CommentCommandService service;


    @PostMapping("{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.comment.command.security.AuthorityType).EDIT_COMMENTS)")
    @Operation(summary = "Оставить комментарий")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201" , description = "Комментарий успешно создан" ,
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"Комментарий создан\"\n" +
                                    "}"))),
                    @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                            content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class)))
            }
    )
    public SuccessResponse createComment(@RequestParam String userId,
                                         @RequestBody CreateCommentCommand command) {
        command.setUserId(userId);
        service.addNewComment(command, SecurityUtil.getCurrentUserId());
        return new SuccessResponse("Комментарий создан");
    }

    @PatchMapping("{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.comment.command.security.AuthorityType).EDIT_COMMENTS)")
    @Operation(summary = "Изменить комментарий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Комментарий успешно отредактирован" ,
            content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                    content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий с таким id не найден",
                    content = @Content(schema = @Schema(implementation = CommentNotFoundException.class)
                            , examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"NOT_FOUND\"\n" +
                            "}")))
    })
    public SuccessResponse editComment(@PathVariable String commentId,
                                       @RequestBody EditCommentCommand command
    ) {
        service.editComment(command, commentId , SecurityUtil.getCurrentUserId());
        return new SuccessResponse("Комментарий успешно изменен");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Комментарий успешно удален" ,
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Комментарий с таким id не найден",
                    content = @Content(schema = @Schema(implementation = CommentNotFoundException.class)
                            , examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"NOT_FOUND\"\n" +
                            "}"))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                    content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class)))
    })
    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.comment.command.security.AuthorityType).EDIT_COMMENTS)")
    @Operation(summary = "Удалить комментарий")
    public SuccessResponse deleteComment(@PathVariable String commentId) {
        service.deleteComment(commentId , SecurityUtil.getCurrentUserId());
        return new SuccessResponse("Комментарий успешно удален");
    }
}
