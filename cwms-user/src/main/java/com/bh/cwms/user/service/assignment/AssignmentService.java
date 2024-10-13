package com.bh.cwms.user.service.assignment;

import java.util.UUID;

public interface AssignmentService {
    void assignRoleToUser(UUID roleId, UUID userId);
}
