package com.bh.cwms.user.model.entity;

import com.bh.cwms.common.bridge.DtoBridge;
import com.bh.cwms.user.model.dto.role.RoleDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLES", uniqueConstraints = {
        @UniqueConstraint(name = "unique_name_in_role", columnNames = {"name"})
})
@Builder
public class Role implements DtoBridge<RoleDto> {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public RoleDto toDto() {
        return new RoleDto(
                id,
                name,
                description
        );
    }
}
