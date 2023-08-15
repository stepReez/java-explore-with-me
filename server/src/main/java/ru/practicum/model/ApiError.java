package ru.practicum.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiError {
    private String errors;

    private String message;

    private String reason;

    private HttpStatus status;

    private String timestamp;
}
