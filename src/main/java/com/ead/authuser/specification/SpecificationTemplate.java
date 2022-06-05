package com.ead.authuser.specification;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    @And({
        @Spec(path = "userType", spec = Equal.class),
        @Spec(path = "email", spec = LikeIgnoreCase.class),
        @Spec(path = "fullname", spec = Like.class),
        @Spec(path = "userStatus", spec = Equal.class)})
    public interface UserSpec extends Specification<UserModel>{}

}
