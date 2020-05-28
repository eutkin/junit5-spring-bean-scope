package io.github.eutkin.scope;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        MultipleMethodTest.TestContextConfig.class
})
@TestScopeBeans
public class MultipleMethodTest {

    private static final Set<String> ACTUAL_VALUES = new ConcurrentSkipListSet<>();

    @Test
    void test1(@Autowired Supplier<String> valueHolder) {
        ACTUAL_VALUES.add(valueHolder.get());
    }

    @Test
    void test2(@Autowired Supplier<String> valueHolder) {
        ACTUAL_VALUES.add(valueHolder.get());
    }

    @Test
    void test3(@Autowired Supplier<String> valueHolder) {
        ACTUAL_VALUES.add(valueHolder.get());
    }

    @AfterAll
    static void afterAll() {
        int uniqueValuesCount = ACTUAL_VALUES.size();
        assertEquals(3, uniqueValuesCount);
    }

    @SpringBootConfiguration
    @DirtiesContext
    static class TestContextConfig {


        @Bean
        List<UUID> valuesIterator() {
            return Stream.generate(UUID::randomUUID).limit(3).collect(toList());
        }

        @Bean
        Iterator<UUID> iterator(List<UUID> list) {
            return list.iterator();
        }

        @Bean
        @Scope(TestScopeBeans.SCOPE_TEST_METHOD)
        String value(Iterator<UUID> iterator) {
            return iterator.next().toString();
        }

        @Bean
        @Scope(TestScopeBeans.SCOPE_TEST_METHOD)
        public Supplier<String> consumer(String value) {
            return value::toUpperCase;
        }
    }
}
