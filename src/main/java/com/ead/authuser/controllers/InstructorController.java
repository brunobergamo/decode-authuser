package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.emuns.RoleType;
import com.ead.authuser.emuns.UserType;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.RoleRepository;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    UserService userService;

    @Autowired
    private RoleRepository roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto){
        Optional<UserModel> userModelOpt = userService.findById(instructorDto.getUserId());
        if(userModelOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not Found"));


        var userModel = userModelOpt.get();
        userModel.setUserType(UserType.INSTRUCTOR);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        userModel.setLastUpdateDate(now);
        userModel.getRoles().add(roleModel);
        userService.updateUser(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
