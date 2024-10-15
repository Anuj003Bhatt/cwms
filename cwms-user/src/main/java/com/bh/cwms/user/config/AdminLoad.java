package com.bh.cwms.user.config;

import com.bh.cwms.common.util.EncryptionUtil;
import com.bh.cwms.user.model.entity.Role;
import com.bh.cwms.user.model.entity.User;
import com.bh.cwms.user.model.type.Status;
import com.bh.cwms.user.repository.RoleRepository;
import com.bh.cwms.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminLoad {
    @Value("${loadSuperAdmin}")
    private Boolean load;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void loadAdmin() {
        if(!load) return;
        try {

            Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
            if (adminRole == null) {
                roleRepository.save(Role.builder().name("ADMIN").description("All Access").build());
            }

            User existingUser1 = userRepository.findByUsernameAndStatus("anuj.bhatt", Status.ACTIVE).orElse(null);
            if (existingUser1 == null) {
                existingUser1 = userRepository.save(
                        User.builder()
                                .username("anuj.bhatt")
                                .name("Anuj")
                                .password(EncryptionUtil.saltEncrypt("1234"))
                                .email("anuj.bhatt@gmail.com")
                                .phone("1234567890")
                                .status(Status.ACTIVE)
                                .build()
                );
            }

            if (!existingUser1.hasRole(adminRole)) {
                existingUser1.addRole(adminRole);
                userRepository.save(existingUser1);
            }

            User existingUser2 = userRepository.findByUsernameAndStatus("user.test", Status.ACTIVE).orElse(null);
            if (existingUser2 == null) {
                existingUser2 = userRepository.save(
                        User.builder()
                                .username("user.test")
                                .name("Test")
                                .password(EncryptionUtil.saltEncrypt("1234"))
                                .email("abc@xyz.com")
                                .phone("1234567891")
                                .status(Status.ACTIVE)
                                .build()
                );
            }

            if (!existingUser2.hasRole(adminRole)) {
                existingUser2.addRole(adminRole);
                userRepository.save(existingUser2);
            }

        } catch (Exception ex){
            log.error("Error while loading admin {}", ex.getMessage());
        }


    }
}
