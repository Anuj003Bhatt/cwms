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
            User user = userRepository.findByUsernameAndStatus("anuj.bhatt", Status.ACTIVE).orElse(
                    userRepository.save(
                            User.builder()
                                    .username("anuj.bhatt")
                                    .name("Anuj")
                                    .password(EncryptionUtil.saltEncrypt("1234"))
                                    .email("anuj.bhatt@gmail.com")
                                    .phone("1234567890")
                                    .status(Status.ACTIVE)
                                    .build()
                    )
            );

            Role role = roleRepository.findByName("ADMIN").orElse(
                    roleRepository.save(Role.builder().name("ADMIN").description("All Access").build())
            );


            if (!user.hasRole(role)) {
                user.addRole(role);
                userRepository.save(user);
            }

        } catch (Exception ex){
            log.error("Error while loading admin {}", ex.getMessage());
        }


    }
}
