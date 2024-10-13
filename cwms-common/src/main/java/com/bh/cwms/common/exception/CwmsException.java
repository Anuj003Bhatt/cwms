package com.bh.cwms.common.exception;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CwmsException extends RuntimeException {
    private String message;
    public CwmsException() {
        super();
    }

    public CwmsException(String message) {
        super(message);
        this.message = message;
    }

    public CwmsException(String message, Object...args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
        this.message = MessageFormatter.arrayFormat(message, args).getMessage();
    }
}
