package com.rm.toolkit.user.query.service;

import com.rm.toolkit.user.query.model.Activity;
import com.rm.toolkit.user.query.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityQueryService {

    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }
}
