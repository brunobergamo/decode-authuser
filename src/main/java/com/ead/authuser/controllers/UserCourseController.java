package com.ead.authuser.controllers;


import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {


    @Autowired
    CourseClient courseClient;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Object> getAllCoursesByUser(@PageableDefault(size = 10, page = 0,sort = "courseId", direction = Sort.Direction.ASC) Pageable page,
                                                      @PathVariable(value = "userId")UUID userId,
                                                      @RequestHeader("Authorization") String token){
        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId,page,token));
    }
}
