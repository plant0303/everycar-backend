package com.road_friends.everycar.user.mapper;

import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDTO> findAllUsers();
}