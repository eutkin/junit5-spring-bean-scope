package io.github.eutkin.scope;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith({CustomSpringExtension.class})
@Import(TestMethodScopeConfiguration.class)
public @interface TestScopeBeans {

    String SCOPE_TEST_METHOD = TestMethodScope.SCOPE_NAME;
}
