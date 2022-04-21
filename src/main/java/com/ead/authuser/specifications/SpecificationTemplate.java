package com.ead.authuser.specifications;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {
    //Anotação que combina os filtros
    @And({
        @Spec(path = "userType", spec = Equal.class),//Patch = parametro que será utilizado para filtrar, SPEC= tipo que vai ser o specification.
        @Spec(path = "email", spec = Like.class),
        @Spec(path = "userStatus", spec = Equal.class),
        @Spec(path = "fullName", spec = Like.class)

    })
    public interface UserSpec extends Specification<UserModel>{}

}
