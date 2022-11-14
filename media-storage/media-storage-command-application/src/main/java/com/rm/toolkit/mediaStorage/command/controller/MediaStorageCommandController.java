package com.rm.toolkit.mediaStorage.command.controller;

import com.rm.toolkit.mediaStorage.command.dto.response.SuccessResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.BadRequestErrorResponse;
import com.rm.toolkit.mediaStorage.command.dto.response.error.FileTooLargeErrorResponse;
import com.rm.toolkit.mediaStorage.command.security.SecurityUtil;
import com.rm.toolkit.mediaStorage.command.service.MediaStorageCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class MediaStorageCommandController {

    private final MediaStorageCommandService mediaStorageCommandService;

    @Operation(summary = "Добавить аватарку пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Файл добавлен успешно",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "  \"message\": \"cf8a4b92-8c2c-40bb-8403-cfa0964e7c47\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "400", description = "Файл не добавлен",
                    content = @Content(schema = @Schema(implementation = BadRequestErrorResponse.class))),
            @ApiResponse(responseCode = "413", description = "Файл слишком большой",
                    content = @Content(schema = @Schema(implementation = FileTooLargeErrorResponse.class)))
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAuthority(T(com.rm.toolkit.mediaStorage.command.security.AuthorityType).EMPLOYEE_CARD)")
    public SuccessResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException, MimeTypeException {
        return new SuccessResponse(mediaStorageCommandService.saveFile(SecurityUtil.getCurrentUserId(), file));
    }
}