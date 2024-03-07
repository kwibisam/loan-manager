package com.kwibisam.loanmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
