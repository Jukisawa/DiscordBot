package com.jukisawa.discordBot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event bus for decoupled communication between components.
 * Allows async processing of events without blocking Discord event thread.
 */
public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);
    
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "EventBus-Worker");
        t.setDaemon(true);
        return t;
    });

    /**
     * Register an event handler for a specific event type
     */
    public <T> void register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        logger.debug("Registered handler for event type: {}", eventType.getSimpleName());
    }

    /**
     * Publish an event to all registered handlers asynchronously
     */
    public <T> CompletableFuture<Void> publish(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers == null || eventHandlers.isEmpty()) {
            logger.debug("No handlers found for event: {}", event.getClass().getSimpleName());
            return CompletableFuture.completedFuture(null);
        }

        logger.debug("Publishing event {} to {} handlers", event.getClass().getSimpleName(), eventHandlers.size());

        // Async processing - doesn't block Discord thread
        CompletableFuture<?>[] futures = eventHandlers.stream()
                .map(handler -> CompletableFuture.runAsync(() -> {
                    try {
                        @SuppressWarnings("unchecked")
                        EventHandler<T> typedHandler = (EventHandler<T>) handler;
                        typedHandler.handle(event);
                    } catch (Exception e) {
                        logger.error("Error handling event {}: ", event.getClass().getSimpleName(), e);
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    /**
     * Shutdown the event bus and cleanup resources
     */
    public void shutdown() {
        executor.shutdown();
        logger.info("EventBus shutdown completed");
    }
}
