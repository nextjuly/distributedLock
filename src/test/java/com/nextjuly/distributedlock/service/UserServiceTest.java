
package com.nextjuly.distributedlock.service;

import com.nextjuly.distributedlock.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyt
 * @version 1.0 created 2020/8/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest{

    @Resource
    private UserService userService;

    @Test
    public void insertUser() {
        String[] arrays = new String[2];
        arrays[0] = "1";
        arrays[1] = "2";

        UserDto userDto = new UserDto();
        userDto.setAge(1);
        userDto.setRealName("nextjuly");
        userDto.setUsername("nextjuly");
        Arrays.stream(arrays).parallel().forEach(info -> {
            userService.insertUser(userDto);
        });
    }

    @Test
    public void updateUser() {UserDto userDto = new UserDto();
        userDto.setAge(111);
        userDto.setRealName("nextjuly111");
        userDto.setUsername("nextjuly");
        UserDto userDto2 = new UserDto();
        userDto2.setAge(222);
        userDto2.setRealName("nextjuly222");
        userDto2.setUsername("nextjuly");
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        userDtoList.add(userDto2);
        userDtoList.parallelStream().forEach(dto -> userService.updateUser(dto));
    }
}