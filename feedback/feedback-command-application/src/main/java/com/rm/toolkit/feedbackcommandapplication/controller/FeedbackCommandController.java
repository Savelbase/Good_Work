package com.rm.toolkit.feedbackcommandapplication.controller;

import com.rm.toolkit.feedbackcommandapplication.dto.command.CreateFeedbackCommand;
import com.rm.toolkit.feedbackcommandapplication.dto.command.EditFeedbackCommand;
import com.rm.toolkit.feedbackcommandapplication.dto.response.success.SuccessResponse;
import com.rm.toolkit.feedbackcommandapplication.security.SecurityUtil;
import com.rm.toolkit.feedbackcommandapplication.service.FeedbackCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/feedbacks")
@RequiredArgsConstructor
@Slf4j
public class FeedbackCommandController {

    private final FeedbackCommandService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.feedbackcommandapplication.security.AuthorityType).EDIT_FEEDBACKS)")
    @Operation(summary = "Оставить фидбек")
    public SuccessResponse createFeedback(@Valid @RequestBody CreateFeedbackCommand command) {
        log.info("Вызван POST /api/v1/feedbacks | userId={}", command.getUserId());

        service.addNewFeedback(command, SecurityUtil.getCurrentUserId());
        return SuccessResponse.getGeneric();
    }

    @PutMapping("/{feedbackId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.feedbackcommandapplication.security.AuthorityType).EDIT_FEEDBACKS)")
    @Operation(summary = "Редактировать фидбек")
    public SuccessResponse editFeedback(@PathVariable String feedbackId,
                                        @Valid @RequestBody EditFeedbackCommand command) {
        log.info("Вызван PUT /api/v1/feedbacks/{}", feedbackId);

        service.editFeedback(command, feedbackId);
        return new SuccessResponse("Фидбек успешно изменен");
    }
}
