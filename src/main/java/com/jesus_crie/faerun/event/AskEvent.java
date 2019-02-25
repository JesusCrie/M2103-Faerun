package com.jesus_crie.faerun.event;

import java.io.Serializable;

/**
 * Represent a type of event that ask for somethings and returns it.
 *
 * @param <T> - The type of object to ask.
 */
public interface AskEvent<T extends Serializable> extends Event {
}
