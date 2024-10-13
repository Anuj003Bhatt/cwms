package com.bh.cwms.user.model.entity;

import com.bh.cwms.common.bridge.DtoBridge;
import com.bh.cwms.common.converter.StringAttributeEncryptor;
import com.bh.cwms.common.model.security.SaltEncrypt;
import com.bh.cwms.user.model.dto.user.UserDto;
import com.bh.cwms.user.model.type.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "PLATFORM_USERS", uniqueConstraints = {
        @UniqueConstraint(name = "platform_users_username_key",  columnNames = {"username"}),
        @UniqueConstraint(name = "platform_users_email",  columnNames = {"email"}),
        @UniqueConstraint(name = "platform_users_phone",  columnNames = {"phone"})
})
public class User implements DtoBridge<UserDto> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    @Convert(converter = StringAttributeEncryptor.class)
    private String email;

    @Column(name = "phone")
    @Convert(converter = StringAttributeEncryptor.class)
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private Status status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "password", nullable = false)
    private SaltEncrypt password;

    @ManyToMany
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> userRoles;

    public Boolean hasRole(Role role) {
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return userRoles.stream().anyMatch(u -> u.getId() == role.getId());
    }

    public void addRole(Role role) {
        if (userRoles == null) {
            this.userRoles = new ArrayList<>();
        }
        this.userRoles.add(role);
    }

    @Override
    public UserDto toDto() {
        UserDto.UserDtoBuilder builder = UserDto.builder()
                .userName(username)
                .name(name)
                .email(email)
                .phone(phone)
                .id(id)
                .status(status);
        if (null != userRoles) {
            builder.roles(userRoles.stream().map(Role::toDto).toList());
        }
        return builder.build();
    }
}
