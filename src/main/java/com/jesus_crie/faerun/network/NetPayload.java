package com.jesus_crie.faerun.network;

import javax.annotation.Nonnull;

public abstract class NetPayload {

    public enum Opcode {
        ASK_USERNAME(0x01, NetPayload.class),
        ASK_SETTINGS(0x02, NetPayload.class),
        ASK_QUEUE(0x03, NetPayload.class),

        SHOW_NEW_ROUND(0x11, NetPayload.class),
        SHOW_CASTLE(0x12, NetPayload.class),
        SHOW_SETTINGS(0x13, NetPayload.class),

        SHOW_FIGHT_START(0x21, NetPayload.class),
        SHOW_FIGHT_END(0x22, NetPayload.class),
        SHOW_FIGHT_HIT(0x23, NetPayload.class),
        SHOW_FIGHT_DEAD(0x24, NetPayload.class),

        UNKNOWN(0xff, NetPayload.class);

        private final int opcode;
        private final Class<? extends NetPayload> clazz;

        Opcode(final int opcode, @Nonnull final Class<? extends NetPayload> clazz) {
            this.opcode = opcode;
            this.clazz = clazz;
        }

        @Nonnull
        public static Opcode fromOpcode(final int code) {
            for (Opcode op : values())
                if (op.opcode == code) return op;
            return UNKNOWN;
        }

        public int getOpcode() {
            return opcode;
        }

        public Class<? extends NetPayload> getPayloadClass() {
            return clazz;
        }
    }

    private final Opcode opcode;

    public NetPayload(@Nonnull final Opcode opcode) {
        this.opcode = opcode;
        // TODO
    }

    public abstract byte[] serialize();
}
