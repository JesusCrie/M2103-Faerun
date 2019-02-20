package com.jesus_crie.faerun.network;

import javax.annotation.Nonnull;

public abstract class NetMessage {

    public enum Opcode {
        ASK_USERNAME((byte) 0x01),
        ASK_SETTINGS((byte) 0x02),
        ASK_QUEUE((byte) 0x03),

        SHOW_NEW_ROUND((byte) 0x11),
        SHOW_CASTLE((byte) 0x12),
        SHOW_SETTINGS((byte) 0x13),

        SHOW_FIGHT_START((byte) 0x21),
        SHOW_FIGHT_END((byte) 0x22),
        SHOW_FIGHT_HIT((byte) 0x23),
        SHOW_FIGHT_DEAD((byte) 0x24);

        private final byte opcode;
        Opcode(final byte opcode) {
            this.opcode = opcode;
        }
    }

    private final Opcode opcode;

    public NetMessage(@Nonnull final Opcode opcode) {
        this.opcode = opcode;
        // TODO
    }
}
