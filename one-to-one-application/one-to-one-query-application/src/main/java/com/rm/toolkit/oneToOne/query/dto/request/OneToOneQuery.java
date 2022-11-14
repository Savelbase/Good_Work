package com.rm.toolkit.oneToOne.query.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneToOneQuery {

    @Schema(example = "4c353585-be59-41a2-9e36-edc7a0b57b3c")
    private String userId;

    @Schema(example = "7368017b-f0d5-4e54-bbe4-72144e4d20e4")
    private String resourceManagerId;

    @Schema(example = "Узнать прогресс в изучении языков Fortran и COBOL")
    private String comment;

    @Schema(example = "2021-08-26")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(example = "15:30")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private boolean isOver;

    private boolean isDeleted;

}
