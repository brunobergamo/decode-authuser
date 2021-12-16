package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {

    boolean existsByUserModelAndCourseId(UserModel userModel, UUID courseId);

    List<UserCourseModel> findByUserModel(UserModel userModel);

    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);

}
