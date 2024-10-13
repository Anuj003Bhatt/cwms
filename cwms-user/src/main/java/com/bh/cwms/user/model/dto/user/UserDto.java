package com.bh.cwms.user.model.dto.user;

import com.bh.cwms.user.model.dto.role.RoleDto;
import com.bh.cwms.user.model.type.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String userName;
    private UUID id;
    private Status status;
    private String name;
    private String email;
    private String phone;
    private List<RoleDto> roles;
}
