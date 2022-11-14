package com.rm.toolkit.comment.query.controller;

import com.rm.toolkit.comment.query.dto.response.error.ForbiddenErrorResponse;
import com.rm.toolkit.comment.query.exception.notfound.CommentNotFoundException;
import com.rm.toolkit.comment.query.model.Comment;
import com.rm.toolkit.comment.query.service.CommentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "Получение комментариев пользователя")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentQueryController {

    private static final int MIN_PAGE_VALUE = 0;
    private static final int MIN_SIZE_VALUE = 1;

    private final CommentQueryService commentService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.comment.query.security.AuthorityType).VIEW_COMMENTS)")
    @Operation(summary = "Получить список всех комментариев с пагинацией и сортировкой по дате (от новых к старым)",
            description = "Страницы начинаются с нуля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Держи список" ,
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                    content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Комментарии пользователя не найдены",
                    content = @Content(schema = @Schema(implementation = CommentNotFoundException.class),
                            examples = @ExampleObject("{\n" +
                            "  \"timestamp\": \"2021-08-20T13:37:19.408Z\",\n" +
                            "  \"errorType\": \"NOT_FOUND\"\n" +
                            "}")))
    })
    public List<Comment> getCommentsForUser(@PathVariable(name = "userId") String userId,
                                            @RequestParam(defaultValue = "0") @Min(MIN_PAGE_VALUE) int page,
                                            @RequestParam(defaultValue = "10") @Min(MIN_SIZE_VALUE) int size) {
        log.info("Вызван GET /api/v1/comments/{}", userId);

        return commentService.getCommentsByUserId(userId, page, size);
    }
}
