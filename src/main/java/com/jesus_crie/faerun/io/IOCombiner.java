package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.AskEvent;
import com.jesus_crie.faerun.event.Event;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Combine a {@link Prompter} and a {@link Listener} and can dispatch some events to each of them.
 */
public final class IOCombiner {

    /* IO */
    private final Prompter in;
    private final Listener out;

    public IOCombiner(@Nonnull final Prompter in,
                      @Nonnull final Listener out) {
        this.in = in;
        this.out = out;
    }

    /**
     * @return The associated prompter.
     */
    @Nonnull
    public Prompter asPrompter() {
        return in;
    }

    /**
     * @return The associated listener.
     */
    @Nonnull
    public Listener asListener() {
        return out;
    }

    /**
     * Dispatch a common event to the listener.
     *
     * @param event - The event to dispatch.
     */
    public void dispatch(@Nonnull final Event event) {
        out.onEvent(event);
    }

    /**
     * Dispatch an {@link AskEvent} to the prompter for a return.
     *
     * @param event - The event to dispatch.
     * @param <T>   - The type of the ask event.
     * @return The mapped object.
     */
    public <T extends Serializable> T dispatch(@Nonnull final AskEvent<T> event) {
        return in.onAsk(event);
    }

    /**
     * Factory method.
     *
     * @param in  - The prompter to use.
     * @param out - The listener to use.
     * @return A new IOCombiner with the given I/O classes.
     */
    @Nonnull
    public static IOCombiner from(@Nonnull final Prompter in, @Nonnull final Listener out) {
        return new IOCombiner(in, out);
    }
}
