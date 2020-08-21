/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.nextjuly.distributedlock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常枚举
 * @author sixiaojie
 * @date 2019-12-23 11:29
 */
@Getter
@AllArgsConstructor
public enum RespStatusEnum {

    /**
     * 200：操作成功
     */
    SUCCESS(200, "操作成功!!"),

    /**
     * 10000+：通用异常code
     */
    SYSTEM_ERROR(10000, "系统异常"),

    /**
     * 审核节点/个人审核数据正在被修改,请稍后刷新页面后再试
     */
    DATA_IS_BEING_MODIFIED(500035, "数据正在被修改,请稍后刷新页面后再试"),

    /**
     * 默认异常
     */
    DEFAULT(0, "系统异常");



    /**
     * 响应code
     */
    private Integer status;
    /**
     * 响应message
     */
    private String message;

    /**
     * 根据key值获取枚举类
     *
     * @param status 异常编号
     * @return respStatusEnum
     */
    public static RespStatusEnum getEnumByKey(Integer status) {
        for (RespStatusEnum respStatusEnum : RespStatusEnum.values()) {
            if (respStatusEnum.getStatus().equals(status)) {
                return respStatusEnum;
            }
        }
        return DEFAULT;
    }
}
