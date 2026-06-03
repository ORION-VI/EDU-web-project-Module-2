package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.mapper.UserMapper;
import org.example.entity.User;
import org.example.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API", description = "API endpoints to manage application users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Operation(summary = "Find user by its ID", description = "Endpoint returns found user with provided ID")
    @ApiResponse(responseCode = "200", description = "User successfully found by provided ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "API user provided invalid ID to API method")
    @ApiResponse(responseCode = "404", description = "User with provided ID has not been found")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> getUserById(@Parameter(description = "Digit ID value of a user to find", example = "202") @PathVariable Long id) {
        try {
            User user = userService.findUser(id);
            EntityModel<UserResponseDto> entityModel = EntityModel.of(userMapper.toDto(user),
                    linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getUsers()).withRel("list_of_all_users"),
                    linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete_user"),
                    linkTo(methodOn(UserController.class).updateUser(id, UserRequestDto.buildUserDto(null, null, null))).withRel("update_user"));
            return ResponseEntity.ok().body(entityModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID USER ID");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
    }

    @Operation(summary = "Find all existing users", description = "Endpoint returns all existing users")
    @ApiResponse(responseCode = "200", description = "Successfully provides list of all existing users",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> getUsers() {
        List<UserResponseDto> userList = userService.findAllUsers().stream().map(userMapper::toDto).toList();
        CollectionModel<UserResponseDto> collectionModel = CollectionModel.of(userList,
                linkTo(methodOn(UserController.class).getUsers()).withSelfRel(),
                Link.of("/api/users/{id}", "find_user_by_id"),
                linkTo(methodOn(UserController.class).createUser(UserRequestDto.buildUserDto(null, null, null))).withRel("create_new_user"));
        return ResponseEntity.ok().body(collectionModel);
    }

    @Operation(summary = "Create new user", description = "Endpoint returns created user with provided parameters")
    @ApiResponse(responseCode = "201", description = "Successfully creates a new user with provided parameters, then returns it",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Failed to create a new user, possibly due to invalid provided parameters")
    @ApiResponse(responseCode = "409", description = "Failed to create a new user because user with provided email already exists")
    @PostMapping
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(@ParameterObject @RequestBody UserRequestDto userDto) {
        try {
            User savedUser = userService.saveUser(userMapper.toEntity(userDto));
            URI savedUserUri = URI.create("/api/users/" + savedUser.getId());
            EntityModel<UserResponseDto> entityModel = EntityModel.of(userMapper.toDto(savedUser),
                    linkTo(methodOn(UserController.class).getUserById(savedUser.getId())).withSelfRel(),
                    linkTo(methodOn(UserController.class).getUsers()).withRel("list_of_all_users"));
            return ResponseEntity.created(savedUserUri).body(entityModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAILED TO CREATE NEW USER");
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "PROVIDED EMAIL ALREADY EXISTS");
        }
    }

    @Operation(summary = "Update existing user by ID with new parameters", description = "Endpoint returns updated user with changed parameters")
    @ApiResponse(responseCode = "200", description = "Successfully updates existing user by ID with provided parameters, then returns it",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Failed to update user, possibly due to invalid provided parameters")
    @ApiResponse(responseCode = "404", description = "Failed to update user because user with provided ID has not been found")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(@Parameter(description = "Digit ID value of existing user", example = "202")
                                                                   @PathVariable Long id, @ParameterObject @RequestBody UserRequestDto userDto) {
        try {
            User updatedUser = userService.updateUser(id, userMapper.toEntity(userDto));
            EntityModel<UserResponseDto> entityModel = EntityModel.of(userMapper.toDto(updatedUser),
                    linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                    Link.of("/api/users/{id}", "find_user_by_id"),
                    linkTo(methodOn(UserController.class).getUsers()).withRel("list_of_all_users"));
            return ResponseEntity.ok(entityModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAILED TO UPDATE USER");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
    }

    @Operation(summary = "Delete existing user by ID", description = "Endpoint deletes user with provided ID")
    @ApiResponse(responseCode = "204", description = "User with provided ID was successfully deleted")
    @ApiResponse(responseCode = "400", description = "Failed to delete user by provided ID, possibly due to invalid ID")
    @ApiResponse(responseCode = "404", description = "Failed to delete user by provided ID because user with provided ID has not been found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "Digit ID value of existing user", example = "202") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAILED TO DELETE USER");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND");
        }
    }
}
