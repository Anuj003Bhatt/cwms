package com.bh.cwms.user.service.role;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.user.model.dto.role.AddRole;
import com.bh.cwms.user.model.dto.role.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoleService {
    ListResponse<RoleDto> getAllRoles(Pageable pageable);
    RoleDto getRoleById(UUID id);
    ListResponse<RoleDto> searchRoleByName(String name, Pageable pageable);
    RoleDto createRole(AddRole role);

}
