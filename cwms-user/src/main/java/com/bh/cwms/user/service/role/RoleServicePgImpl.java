package com.bh.cwms.user.service.role;

import com.bh.cwms.common.exception.BadRequestException;
import com.bh.cwms.common.exception.NotFoundException;
import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.common.util.BridgeUtil;
import com.bh.cwms.user.model.dto.role.AddRole;
import com.bh.cwms.user.model.dto.role.RoleDto;
import com.bh.cwms.user.model.entity.Role;
import com.bh.cwms.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServicePgImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public ListResponse<RoleDto> getAllRoles(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        return BridgeUtil.buildPaginatedResponse(roles);
    }

    @Override
    public RoleDto getRoleById(UUID id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Role with id '{}' not found",id)
        ).toDto();
    }

    @Override
    public ListResponse<RoleDto> searchRoleByName(String name, Pageable pageable) {
        Page<Role> roles = roleRepository.findByNameContaining(name, pageable);
        return BridgeUtil.buildPaginatedResponse(roles);
    }

    @Override
    public RoleDto createRole(AddRole newRole) {
        Role existingRole = roleRepository.findByName(newRole.getName()).orElse(null);

        if (existingRole != null) {
            throw new BadRequestException("Role with the name '{}' already exists.", newRole.getName());
        }
        Role role = Role.builder()
                .name(newRole.getName())
                .description(newRole.getDescription())
                .build();
        return roleRepository.save(role).toDto();
    }
}
