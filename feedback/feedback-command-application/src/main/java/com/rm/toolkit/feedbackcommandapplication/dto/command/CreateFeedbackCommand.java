package com.rm.toolkit.feedbackcommandapplication.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackCommand {

    private static final int MIN_ASSESSMENT_VALUE = 1;
    private static final int MAX_ASSESSMENT_VALUE = 5;

    private String id;

    private ZonedDateTime dateTime;

    private String userId;

    private String resourceManagerId;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int overAllAssessment;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int professionalSkills;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int workQuality;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int criticalThinking;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int reliability;

    @Min(value = MIN_ASSESSMENT_VALUE, message = "min оценка не может быть меньше " + MIN_ASSESSMENT_VALUE)
    @Max(value = MAX_ASSESSMENT_VALUE, message = "max оценка не может быть больше " + MAX_ASSESSMENT_VALUE)
    private int communicationSkills;

    private String development;

    private String project;

    private String goals;

    private String department;

    private String activities;

    private String additionally;

    private String oneToOneId;
}
