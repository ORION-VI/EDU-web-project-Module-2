package org.example.controller;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.mapper.UserMapper;
import org.example.entity.User;
import org.example.service.UserService;
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
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> getUserById(@PathVariable Long id) {
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

    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> getUsers() {
        List<UserResponseDto> userList = userService.findAllUsers().stream().map(userMapper::toDto).toList();
        CollectionModel<UserResponseDto> collectionModel = CollectionModel.of(userList,
                linkTo(methodOn(UserController.class).getUsers()).withSelfRel(),
                Link.of("/api/users/{id}", "find_user_by_id"),
                linkTo(methodOn(UserController.class).createUser(UserRequestDto.buildUserDto(null, null, null))).withRel("create_new_user"));
        return ResponseEntity.ok().body(collectionModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(@RequestBody UserRequestDto userDto) {
        try {
            User savedUser = userService.saveUser(userMapper.toEntity(userDto));
            URI savedUserUri = URI.create("/api/users/" + savedUser.getId());
            EntityModel<UserResponseDto> entityModel = EntityModel.of(userMapper.toDto(savedUser),
                    linkTo(methodOn(UserController.class).getUserById(savedUser.getId())).withSelfRel(),
                    linkTo(methodOn(UserController.class).getUsers()).withRel("list_of_all_users"));
            return ResponseEntity.created(savedUserUri).body(entityModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FAILED TO CREATE NEW USER");
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "PROVIDED EMAIL ALREADY EXISTS");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userDto) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
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
