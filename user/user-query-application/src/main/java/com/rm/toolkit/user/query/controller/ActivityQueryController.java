package com.rm.toolkit.user.query.controller;

import com.rm.toolkit.user.query.model.Activity;
import com.rm.toolkit.user.query.service.ActivityQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Получение информации об активностях")
@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivityQueryController {

    private final ActivityQueryService activityQueryService;

    @Operation(summary = "Получить список всех активностей, которые можно назначить пользователю")
    @GetMapping
    public List<Activity> getAllActivities() {
        log.info("Вызван GET /api/v1/activities");

        return activityQueryService.getAllActivities();
    }
}
