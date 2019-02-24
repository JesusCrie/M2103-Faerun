package com.jesus_crie.faerun.event;

import java.io.Serializable;

/**
 * Represent an event of the game that can also be serialized and sent over the network.
 * It must contains only pieces of information and no complex classes.
 */
public interface GameEvent extends Serializable {
}
