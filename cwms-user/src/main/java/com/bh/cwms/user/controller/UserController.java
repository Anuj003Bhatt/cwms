package com.bh.cwms.user.controller;

import com.bh.cwms.common.model.rest.response.ListResponse;
import com.bh.cwms.user.model.dto.user.AddUser;
import com.bh.cwms.user.model.dto.user.UserAuthenticationRequest;
import com.bh.cwms.user.model.dto.user.UserDto;
import com.bh.cwms.user.service.assignment.AssignmentService;
import com.bh.cwms.user.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Tag(name = "Users")
public class UserController {
    private final UserService userService;
    private final AssignmentService assignmentService;

    @GetMapping("/_byId/{id}")
    @Operation(
            summary = "Fetch user by ID",
            description = "Find a user by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user by ID"),
            @ApiResponse(responseCode = "404", description = "No user found for given ID")
    })
    public UserDto findUserById(@PathVariable("id") UUID id){
        return userService.getUserById(id);
    }

    @GetMapping
    @Operation(
            summary = "List users",
            description = "Find the list of all users"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user by ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ListResponse<UserDto> getUsersPage(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable){
        return userService.getPaginated(pageable);
    }

    @PostMapping("signup")
    @Operation(
            summary = "Create new user",
            description = "Create a new user for the platform"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User added successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirements()
    public UserDto createUser(@RequestBody AddUser user){
        return userService.addUser(user);
    }

    @PatchMapping("/{id}/disable")
    @Operation(
            summary = "Disable an existing user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User disabled successfully"),
            @ApiResponse(responseCode = "404", description = "No user found for given ID")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, String> disableUser(@PathVariable("id") UUID id){
        userService.disableUser(id);
        return Map.of("message",String.format("User %s has been disabled", id));
    }

    @PatchMapping("/{id}/enable")
    @Operation(
            summary = "Enable an already disabled user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User enabled successfully"),
            @ApiResponse(responseCode = "404", description = "No disabled user found for given ID")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, String> enableUser(@PathVariable("id")UUID id){
        userService.enableUser(id);
        return Map.of("message",String.format("User %s has been enabled", id));
    }

    @PostMapping("authenticate")
    @Operation(summary = "Authenticate a user by username and password and return a JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @SecurityRequirements()
    public Map<String, String> authenticate(
            @RequestBody @Valid UserAuthenticationRequest request
    ) {
        return userService.authenticate(request.getUsername(), request.getPassword());
    }

    @Operation(
            summary = "Assign Role",
            description = "Assign a role to a user"
    )
    @PutMapping("/{userId}/role/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, String> assignUserToRole(
            @PathVariable("userId") UUID userId,
            @PathVariable("roleId") UUID roleId
    ){
        assignmentService.assignRoleToUser(roleId, userId);
        return Map.of("message","Role has been assigned to the user.");
    }
}
