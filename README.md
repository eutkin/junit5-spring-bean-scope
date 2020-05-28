# Spring Bean Scope for JUnit 5 Test Method

The test method scope creates a bean instance for a single 
test method execution.

```java
import io.github.eutkin.scope.TestScopeBeans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;import org.springframework.context.ApplicationListener;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Scope;import org.springframework.context.event.ContextStartedEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = SampleTest.TestConfig.class)
@TestScopeBeans
public class SampleTest {

    // the latch will be every time a new one
    @Test
    public void testEventHappened(@Autowired CountDownLatch latch) throws Exception {
        boolean await = latch.await(30, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
    }
    
    // the latch will be every time a new one
    @Test
    public void testEventDidntHappen(@Autowired CountDownLatch latch) throws Exception {
        boolean await = latch.await(30, TimeUnit.SECONDS);
        Assertions.assertFalse(await);
    }

    @SpringBootConfiguration
    static class TestConfig {
        
        @Bean
        @Scope(TestScopeBeans.SCOPE_TEST_METHOD)
        CountDownLatch latch() {
            return new CountDownLatch(1);
        }
        
        @Bean
        @Scope(TestScopeBeans.SCOPE_TEST_METHOD)
        ApplicationListener<ContextStartedEvent> listener(CountDownLatch latch) {
            return event -> latch.countDown();
        }

        @Bean
        @Scope(TestScopeBeans.SCOPE_TEST_METHOD)
        ApplicationListener<NeverTriggeredEvent> neverListener(CountDownLatch latch) {
            return event -> latch.countDown();
        }
    }   
}
```

Supported ParametrizedTest.