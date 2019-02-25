package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.utils.Pair;

/**
 * Ask the local player for the address of the server.
 */
public class AskRemoteAddress implements AskEvent<Pair<String, Integer>> {
    private static final long serialVersionUID = -5763465712561359050L;
}
