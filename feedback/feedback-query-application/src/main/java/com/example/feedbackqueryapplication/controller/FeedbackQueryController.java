package com.example.feedbackqueryapplication.controller;

import com.example.feedbackqueryapplication.dto.response.error.ForbiddenErrorResponse;
import com.example.feedbackqueryapplication.dto.response.error.NotFoundErrorResponse;
import com.example.feedbackqueryapplication.model.Feedback;
import com.example.feedbackqueryapplication.security.jwt.JwtUtil;
import com.example.feedbackqueryapplication.service.FeedbackQueryService;
import com.example.feedbackqueryapplication.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Получение фидбеков на сотрудника")
@Slf4j
public class FeedbackQueryController {

    private static final int MIN_PAGE_VALUE = 0;
    private static final int MIN_SIZE_VALUE = 1;

    private final FeedbackQueryService feedbackService;
    private final UserQueryService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority(T(com.example.feedbackqueryapplication.security.AuthorityType).VIEW_FEEDBACKS)")
    @Operation(summary = "Получить список всех фидбеков с пагинацией и сортировкой по дате (от новых к старым)",
            description = "Страницы начинаются с нуля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи список",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Feedback.class)))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                    content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Данные не найдены",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    public List<Feedback> getFeedbacksForUser(@PathVariable(name = "userId") String userId,
                                              @RequestParam(defaultValue = "0") @Min(MIN_PAGE_VALUE) int page,
                                              @RequestParam(defaultValue = "10") @Min(MIN_SIZE_VALUE) int size,
                                              @RequestHeader(value = "Authorization", required = false)
                                              @Parameter(hidden = true) String authorizationHeader) {

        log.info("Вызван GET /api/v1/feedbacks/users/{} | page={}, size={}", userId, page, size);

        int managerRolePriority = jwtUtil.getRolePriority(jwtUtil.getTokenFromHeader(authorizationHeader));

        boolean enableGetFeedbacks = userService.isUserInPoolOfSubordinates(managerRolePriority, userId);
        if (enableGetFeedbacks) {
            return feedbackService.getFeedbacksByUserId(userId, page, size);

        } else {
            log.error("Пользователь Id {} не находится в пуле подчиненных", userId);
            throw new AccessDeniedException("Пользователь не находится в пуле подчиненных");
        }
    }

    @GetMapping("/{feedbackId}")
    @PreAuthorize("hasAuthority(T(com.example.feedbackqueryapplication.security.AuthorityType).VIEW_FEEDBACKS)")
    @Operation(summary = "Получить фидбек по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Держи фидбек",
                    content = @Content(schema = @Schema(implementation = Feedback.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав для совершения данного действия",
                    content = @Content(schema = @Schema(implementation = ForbiddenErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Данные не найдены",
                    content = @Content(schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    public Feedback getFeedback(@PathVariable(name = "feedbackId") String feedbackId,
                                @RequestParam String userId,
                                @RequestHeader(value = "Authorization", required = false)
                                @Parameter(hidden = true) String authorizationHeader) {

        log.info("Вызван GET /api/v1/feedbacks/{}", feedbackId);

        int managerRolePriority = jwtUtil.getRolePriority(jwtUtil.getTokenFromHeader(authorizationHeader));

        boolean enableGetFeedbacks = userService.isUserInPoolOfSubordinates(managerRolePriority, userId);
        if (enableGetFeedbacks) {
            Feedback feedback = feedbackService.getFeedbackById(feedbackId);

            if (feedback.getUserId().equals(userId)) {
                return feedback;
            } else {
                log.error("userId {} не принадлежит feedbackId {}", userId, feedbackId);
                throw new AccessDeniedException(String.format("userId %s не принадлежит feedbackId %s", userId, feedbackId));
            }

        } else {
            log.error("Пользователь Id {} не находится в пуле подчиненных", userId);
            throw new AccessDeniedException("Пользователь не находится в пуле подчиненных");
        }
    }
}
