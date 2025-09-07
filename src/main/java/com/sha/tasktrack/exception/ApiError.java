package com.sha.tasktrack.exception;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
