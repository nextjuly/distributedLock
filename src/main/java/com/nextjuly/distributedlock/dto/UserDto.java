package com.nextjuly.distributedlock.dto;

import com.nextjuly.distributedlock.annotation.DataLock;
import lombok.Data;

/**
 * @author wangyt
 * @version 1.0 created 2020/8/19
 */
@DataLock
@Data
public class UserDto {

    /**
     * 用户名
     */
    @DataLock
    private String username;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 真实姓名
     */
    @DataLock
    private String realName;
}
