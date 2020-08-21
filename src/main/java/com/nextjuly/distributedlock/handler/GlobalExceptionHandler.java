package com.nextjuly.distributedlock.handler;

import com.nextjuly.distributedlock.exception.BaseException;
import com.nextjuly.distributedlock.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 提供给前台的
 *
 * @author pumengxin
 * @date 2019年02月19日 11:15:58
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final String REAL_HTTP_STATUS = "real_http_status";
    private static final String ERROR_MESSAGE = "error_message";

    /**
     * 禁止访问错误
     */
    private static final int TOKEN_FORBIDDEN_CODE = 40301;
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 500- 业务异常
     */
    @ExceptionHandler(BaseException.class)
    public RestResponse baseExceptionHandler(HttpServletResponse response, BaseException ex) throws UnsupportedEncodingException {
        logger.error(ex.getMessage(), ex);
        response.addHeader(REAL_HTTP_STATUS, String.valueOf(ex.getStatus()));
        if (ex.getMessage() != null) {
            response.addHeader(ERROR_MESSAGE, URLEncoder.encode(ex.getMessage(), "UTF-8"));
        }
        // 如果错误码存在
        if (ex.getStatus() == TOKEN_FORBIDDEN_CODE) {
            return RestResponse.fail(TOKEN_FORBIDDEN_CODE, ex.getMessage());
        }
        return RestResponse.fail(ex.getStatus(), ex.getMessage());
    }


    /**
     * 500- server error
     */
    @ExceptionHandler(Exception.class)
    public RestResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return RestResponse.fail(500, "系统繁忙 " + ex.toString());
    }

    /**
     * 缺少请求参数- Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestResponse handleMissingServletRequestParameterException(HttpServletResponse response, MissingServletRequestParameterException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 参数解析失败- Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RestResponse handleHttpMessageNotReadableException(HttpServletResponse response, HttpMessageNotReadableException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 参数验证失败 - Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse handleMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数绑定失败- Bad Request
     */
    @ExceptionHandler(BindException.class)
    public RestResponse handleBindException(HttpServletResponse response, BindException ex) {
        logger.error(ex.getMessage(), ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 400 - 参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RestResponse processArgumentTypeMismatchException(HttpServletResponse response, MethodArgumentTypeMismatchException ex) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 415 - 媒体类型不匹配
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public RestResponse handleHttpMediaTypeNotSupportedException(HttpServletResponse response, HttpMediaTypeNotSupportedException ex) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage());
    }

    /**
     * 405 - 请求方法不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestResponse handleHttpRequestMethodNotSupportedException(HttpServletResponse response, HttpRequestMethodNotSupportedException ex) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return RestResponse.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage());
    }
}
