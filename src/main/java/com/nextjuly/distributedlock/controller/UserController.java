package com.nextjuly.distributedlock.controller;

import com.nextjuly.distributedlock.dto.UserDto;
import com.nextjuly.distributedlock.rest.RestResponse;
import com.nextjuly.distributedlock.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyt
 * @version 1.0 created 2020/8/19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增用户
     *
     * @param userDto 用户信息
     * @author wangyt created 2020/8/19
     */
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public RestResponse<Boolean> insertUser(@RequestBody UserDto userDto) {
        return RestResponse.success(userService.insertUser(userDto));
    }

    /**
     * 修改用户
     *
     * @param userDto 用户信息
     * @author wangyt created 2020/8/19
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    public RestResponse<Boolean> updateUser(@RequestBody UserDto userDto) {
        return RestResponse.success(userService.updateUser(userDto));
    }

    /**
     * 查询用户列表
     *
     * @param userDto 用户信息
     * @author wangyt created 2020/8/19
     */
    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    public RestResponse<List<UserDto>> listUsers() {
        return RestResponse.success(userService.listUsers());
    }
}
