package com.ead.authuser.controllers;


import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(@PageableDefault(size = 10, page = 0,sort = "courseId", direction = Sort.Direction.ASC) Pageable page,
                                                               @PathVariable(value = "userId")UUID userId){

        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId,page));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionInCourse(@PathVariable(value = "userId")UUID userId,
                                                           @RequestBody @Valid UserCourseDto userCourseDto){

        Optional<UserModel> userModel = userService.findById(userId);
        if(userModel.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        if(userCourseService.existsByUserAndCourseId(userModel.get(), userCourseDto.getCourseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Subscription already exists");
        }

        userCourseService.save(userModel.get().convertToUserCourseModel(userCourseDto.getCourseId()));

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created");

    }
}
