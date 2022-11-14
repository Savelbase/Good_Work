package com.rm.toolkit.mediaStorage.query.controller;

import com.rm.toolkit.mediaStorage.query.dto.response.error.NotFoundErrorResponse;
import com.rm.toolkit.mediaStorage.query.service.MediaStorageQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class MediaStorageQueryController {

    private final MediaStorageQueryService mediaStorageQueryService;

    @Operation(summary = "Получить аватар по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Файл с таким id не найден", content = @Content(
                    schema = @Schema(implementation = NotFoundErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getFileById(@PathVariable String id) {
        log.info("Получаем файл по id: {}", id);
        return mediaStorageQueryService.loadFileById(id);
    }
}
