package com.nextjuly.distributedlock.exception;


import com.nextjuly.distributedlock.enums.RespStatusEnum;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author wangyiting
 * @version 1.0 created 2019/12/4
 */
public class BaseException extends RuntimeException {
    private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private Object[] params;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BaseException() {
    }

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    private BaseException(String message, String status) {
        super(message);
        this.status = Integer.parseInt(status);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.status = code;
    }

    public BaseException(RespStatusEnum result) {
        super(result.getMessage());
        status = result.getStatus();
    }

    public BaseException(RespStatusEnum result, Throwable cause) {
        this(result.getStatus(), result.getMessage(), cause);
    }

    public BaseException(RespStatusEnum result, Object... params) {
        this(MessageFormat.format(result.getMessage(), params), result.getStatus());
        this.params = params;
    }

    public BaseException(RespStatusEnum result, Throwable cause, Object... params) {
        this(result.getStatus(), MessageFormat.format(result.getMessage(), params), cause);
        this.params = params;
    }
}
