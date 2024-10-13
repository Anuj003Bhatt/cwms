package com.bh.cwms.user.service.user;

import com.bh.cwms.common.exception.AuthenticationFailedException;
import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.exception.NotFoundException;
import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.util.BridgeUtil;
import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.common.util.JwtUtil;
import com.bh.cwms.user.model.dto.user.AddUser;
import com.bh.cwms.user.model.dto.user.UserDto;
import com.bh.cwms.user.model.entity.Role;
import com.bh.cwms.user.model.entity.User;
import com.bh.cwms.user.model.type.Status;
import com.bh.cwms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServicePgImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(AddUser newUser) {
        User existingUser = userRepository.findByUsernameOrPhoneOrEmail(newUser.getUserName(), newUser.getPhone() ,newUser.getEmail())
                .orElse(null);

        if(existingUser != null) {
            throw new BadRequestException("User with same email/phone/username already exists");
        }

        User user = User.builder()
                .username(newUser.getUserName())
                .name(newUser.getName())
                .password(EncryptionUtil.saltEncrypt(newUser.getPassword()))
                .email(newUser.getEmail())
                .phone(newUser.getPhone())
                .status(newUser.getStatus())
                .build();
        return userRepository.save(user).toDto();
    }

    @Override
    public UserDto getUserById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("No user found for id {}", userId)
                )
                .toDto();
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userRepository
                .findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(
                        () -> new NotFoundException("No active user found with the username '{}'", username)
                )
                .toDto();
    }

    @Override
    public Map<String, String> authenticate(String username, String password) {
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE).orElseThrow(
                () -> new NotFoundException("No user found for the username: '{}'", username)
        );
        if (EncryptionUtil.verifyPassword(password, user.getPassword())) {
            // return token
            return Map.of(
                "token",
                JwtUtil.generateToken(
                        user.getUsername(),
                        user.getId(),
                        user.getUserRoles().stream().map(Role::getName).toList()
                )
            );
        }
        throw new AuthenticationFailedException("Invalid Credentials");
    }

    @Override
    public void disableUser(UUID userId) {
        User user = userRepository.findByIdAndStatus(userId, Status.ACTIVE).orElseThrow(
                () -> new NotFoundException("User with id {} not found or already disabled", userId)
        );
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public void enableUser(UUID userId) {
        User user = userRepository.findByIdAndStatus(userId, Status.INACTIVE).orElseThrow(
                () -> new NotFoundException("User with id {} not found or already enabled", userId)
        );
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public ListResponse<UserDto> getPaginated(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return BridgeUtil.buildPaginatedResponse(users);
    }
}
