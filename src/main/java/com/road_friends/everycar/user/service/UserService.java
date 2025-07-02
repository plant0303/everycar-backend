package com.road_friends.everycar.user.service;

import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    public class UserService {

        private final UserMapper userMapper;

        public UserService(UserMapper userMapper) {
            this.userMapper = userMapper;
        }

        public List<UserDTO> getAllUsers() {
            return userMapper.findAllUsers();
        }
    }
