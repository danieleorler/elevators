package com.tingco.codechallenge.elevator;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Preconfigured Spring Application boot class.
 *
 */
@SpringBootApplication
public class ElevatorApplication {

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args
     *            Not in use
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }

    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
    @Bean(destroyMethod = "shutdown")
    @Qualifier("elevatorExecutor")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(numberOfElevators);
    }

    /**
     * Create an event bus for your convenience.
     *
     * @return EventBus for async task execution
     */
    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }

    @Bean
    public List<Elevator> elevators(EventBus eventBus) {
        return IntStream
            .range(0, numberOfElevators)
            .mapToObj(value -> new ElevatorImpl(value, eventBus))
            .collect(Collectors.toList());
    }

}
