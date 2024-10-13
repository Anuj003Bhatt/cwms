package com.bh.cwms.user.service.assignment;

import com.bh.cwms.common.exception.NotFoundException;
import com.bh.cwms.user.model.entity.Role;
import com.bh.cwms.user.model.entity.User;
import com.bh.cwms.user.repository.RoleRepository;
import com.bh.cwms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void assignRoleToUser(UUID roleId, UUID userId) {
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new NotFoundException("Role with id '{}' not found",roleId)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id '{}' not found",userId)
        );
        if (!user.hasRole(role)) {
            user.addRole(role);
            userRepository.save(user);
        }
    }
}
