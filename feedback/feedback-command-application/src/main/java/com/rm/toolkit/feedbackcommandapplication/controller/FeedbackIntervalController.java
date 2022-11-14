package com.rm.toolkit.feedbackcommandapplication.controller;

import com.rm.toolkit.feedbackcommandapplication.dto.response.success.SuccessResponse;
import com.rm.toolkit.feedbackcommandapplication.service.FeedbackIntervalCommandService;
import com.rm.toolkit.feedbackcommandapplication.service.FeedbackNotificationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/feedbacks/interval")
@RequiredArgsConstructor
@Slf4j
public class FeedbackIntervalController {

    private static final int MIN_INTERVAL_VALUE = 1;
    private static final int MAX_INTERVAL_VALUE = 9;

    private final FeedbackIntervalCommandService service;
    private final FeedbackNotificationCommandService notificationCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.feedbackcommandapplication.security.AuthorityType).EDIT_INTERVALS)")
    @Operation(summary = "Установить интервал для оставления фидбека")
    public SuccessResponse setIntervalForFeedback(@RequestParam
                                                   @Min(MIN_INTERVAL_VALUE) @Max(MAX_INTERVAL_VALUE) Integer interval) {
        log.info("Вызван POST /api/v1/feedbacks/interval | interval={}", interval);

        service.setIntervalForFeedback(interval);

        return new SuccessResponse("Интервал для фидбека успешно установлен");
    }
}
