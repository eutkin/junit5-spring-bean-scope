package io.github.eutkin.scope;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        ParametrizedMethodTest.TestContextConfig.class
})
@TestScopeBeans
public class ParametrizedMethodTest {

    static Stream<Arguments> provider() {
        return Stream.of("foo", "bar", "kar").map(Arguments::of);
    }

    private static final Map<String, String> ACTUAL_VALUES = new ConcurrentHashMap<>(3);

    @ParameterizedTest
    @MethodSource("provider")
    void test(
            String arg, @Autowired Supplier<String> valueHolder
    ) {
        ACTUAL_VALUES.put(arg, valueHolder.get());
    }

    @AfterAll
    static void afterAll() {
        int uniqueValuesCount = new HashSet<>(ACTUAL_VALUES.values()).size();
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
