package com.rafaelsousa.post.service.api.exception;

import lombok.Getter;

@Getter
public enum ExceptionResponseType {
    RESOURCE_NOT_FOUND("Resource not found", "/resource-not-found"),
    INVALID_DATA("Invalid data", "/invalid-data");

    private final String title;
    private final String uri;

    ExceptionResponseType(String title, String path) {
        this.uri = "https://algaposts.com.br".concat(path);
        this.title = title;
    }
}