package dev.chijiokeibekwe.librarymanagementsystem.annotation;

import dev.chijiokeibekwe.librarymanagementsystem.config.WithMockUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {

    String id() default "1";

    String email() default "jane.doe@library.com";

    String phone() default "+2348012345678";

}
