package com.rm.toolkit.emailsender.command.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SubjectType {
    CREATED_SUBJECT("Запланирована встреча one-to-one"),
    UPDATED_SUBJECT("Изменена встреча one-to-one"),
    DELETED_SUBJECT("Удалена встреча one-to-one"),
    NO_FEEDBACK_PROVIDED_SUBJECT("Не предоставлен фидбэк на %s %s"),
    REPEATED_NO_FEEDBACK_PROVIDED_SUBJECT("Повторно! Не предоставлен фидбэк на %s %s");

    private final String subject;
}