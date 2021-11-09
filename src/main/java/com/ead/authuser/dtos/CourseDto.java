package com.ead.authuser.dtos;

import com.ead.authuser.emuns.CourseLevel;
import com.ead.authuser.emuns.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CourseDto {

    private UUID courseID;

    private String name;

    private String description;

    private String imageUrl;

    private CourseStatus courseStatus;

    private UUID userInstructor;

    private CourseLevel courseLevel;
}
