package com.jukisawa.discordBot.event;

/**
 * Functional interface for handling events in the event bus system
 */
@FunctionalInterface
public interface EventHandler<T> {
    /**
     * Handle the given event
     * @param event The event to handle
     */
    void handle(T event);
}
