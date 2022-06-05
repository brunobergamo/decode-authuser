package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specification.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable){
        Page<UserModel> userModelPage =  userService.findAll(pageable,spec);

        userModelPage.stream().map(userModel -> userModel.add(linkTo(methodOn(UserController.class).getUser(userModel.getUserId())).withSelfRel())).collect(Collectors.toList());
        ResponseEntity<Page<UserModel>> body = ResponseEntity.status(HttpStatus.OK).body(userModelPage);
        return body;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable (value = "userId")UUID userId){
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModel.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable (value = "userId")UUID userId){
        log.debug("DELETE deleteUser userId received {} ", userId);
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isPresent()){
            userService.deleteUser(userModel.get());
            log.debug("DELETE deleteUser userId deleted {} ", userId);
            log.info("User deleted successfully userId {} ", userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable (value = "userId")UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.UserPut.class)  @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDto){

        log.debug("PUT updateUser userDto received {} ", userDto.toString());
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        var user = userModel.get();
        user.setCpf(userDto.getCpf());
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updateUser(user);
        log.debug("PUT updateUser userModel saved {} ", user.toString());
        log.info("User updated successfully userId {} ", user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable (value = "userId")UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.PasswordPut.class) @JsonView(UserDTO.UserView.PasswordPut.class) UserDTO userDto){
        log.debug("PUT updatePassword userDto received {} ", userDto.toString());
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        if(!userModel.get().getPassword().equals(userDto.getOldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismastchedols password");
        }

        var user = userModel.get();
        user.setPassword(userDto.getPassword());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updatePassword(user);
        log.debug("PUT updatePassword userModel saved {} ", user.toString());
        log.info("Password updated successfully userId {} ", user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated sucessfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable (value = "userId")UUID userId,
                                              @RequestBody @Validated(UserDTO.UserView.ImagePut.class) @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDto){
        log.debug("PUT updateImage userDto received {} ", userDto.toString());
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        var user = userModel.get();
        user.setImageUrl(userDto.getImageUrl());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.updateUser(user);
        log.debug("PUT updateImage userModel saved {} ", user.toString());
        log.info("Image updated successfully userId {} ", user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
