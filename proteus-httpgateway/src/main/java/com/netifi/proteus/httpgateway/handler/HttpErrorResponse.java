package com.netifi.proteus.httpgateway.handler;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class HttpErrorResponse implements Serializable {
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Integer httpStatus;
    private String httpStatusMessage;
    private String timestamp;
    private String message;

    public static HttpErrorResponse of(HttpStatus httpStatus, String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN)
                .withZone(ZoneId.systemDefault());

        HttpErrorResponse response = new HttpErrorResponse();
        response.setHttpStatus(httpStatus.value());
        response.setHttpStatusMessage(httpStatus.getReasonPhrase());
        response.setTimestamp(dtf.format(Instant.now()));
        response.setMessage(message);

        return response;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public void setHttpStatusMessage(String httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
