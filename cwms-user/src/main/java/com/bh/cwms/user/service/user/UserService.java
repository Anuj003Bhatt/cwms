package com.bh.cwms.user.service.user;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.user.model.dto.user.AddUser;
import com.bh.cwms.user.model.dto.user.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    UserDto addUser(AddUser user);
    UserDto getUserById(UUID userId);
    UserDto getUserByUsername(String username);
    Map<String, String> authenticate(String username, String password);
    void disableUser(UUID userId);
    void enableUser(UUID userId);
    ListResponse<UserDto> getPaginated(Pageable pageable);

}
