package io.github.eutkin.scope;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TestMethodScopeConfiguration {

    @Bean
    public static BeanFactoryPostProcessor testMethodScopeBeanFactoryBeanPostProcessor() {
        return factory -> factory.registerScope(TestMethodScope.SCOPE_NAME, TestMethodScope.INSTANCE);
    }
}
