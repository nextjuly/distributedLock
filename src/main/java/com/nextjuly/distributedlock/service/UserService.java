package com.nextjuly.distributedlock.service;

import com.nextjuly.distributedlock.dto.UserDto;

import java.util.List;

/**
 * @author wangyt
 * @version 1.0 created 2020/8/19
 */
public interface UserService {

    /**
     * 新增用户
     *
     * @param userDto 用户信息
     * @author wangyt created 2020/8/19
     */
    Boolean insertUser(UserDto userDto);

    /**
     * 修改用户信息
     *
     * @author wangyt created 2020/8/19
     * @param userDto 用户信息
     */
    Boolean updateUser(UserDto userDto);

    /**
     * 查询用户列表
     * 
     * @author wangyt created 2020/8/19
     * @return list
     */
    List<UserDto> listUsers();
}
