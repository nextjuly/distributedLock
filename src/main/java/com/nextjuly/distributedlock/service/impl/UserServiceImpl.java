package com.nextjuly.distributedlock.service.impl;

import com.nextjuly.distributedlock.annotation.RedisLock;
import com.nextjuly.distributedlock.dto.UserDto;
import com.nextjuly.distributedlock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户操作service
 *
 * @author wangyt
 * @version 1.0 created 2020/8/19
 */
@Service
public class UserServiceImpl implements UserService {

    List<UserDto> userDtos = new ArrayList<>();

    UserDto userDto = new UserDto();

    @Override
    @RedisLock(prefix = "user-add", parameterSequenceNumbers = {0}, keepMills = 5000, action = RedisLock.LockFailAction.GIVEUP)
    public Boolean insertUser(UserDto userDto) {
        return userDtos.add(userDto);
    }

    @Override
    @RedisLock(prefix = "user-update", parameterSequenceNumbers = {0}, keepMills = 5000, action = RedisLock.LockFailAction.GIVEUP)
    public Boolean updateUser(UserDto userDto) {
        userDto.setUsername(userDto.getUsername());
        return true;
    }

    @Override
    public List<UserDto> listUsers() {
        return userDtos;
    }
}
