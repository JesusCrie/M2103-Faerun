package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.network.NetDeSerializer;

import javax.annotation.Nonnull;

public abstract class NetPayload {

    /**
     * Size of the reserved space at the beginning of the payload
     * containing the metadata (header).
     */
    protected static final int OFFSET_RESERVED = 3;

    public enum Opcode {
        ASK_USERNAME(0x01, AskUsernamePayload.class),
        ASK_SETTINGS(0x02, AskSettingsPayload.class),
        ASK_QUEUE(0x03, AskQueuePayload.class),

        SHOW_NEW_ROUND(0x11, ShowNewRoundPayload.class),
        SHOW_CASTLE(0x12, ShowCastlePayload.class),
        SHOW_SETTINGS(0x13, ShowSettingsPayload.class),

        SHOW_FIGHT_START(0x21, ShowFightStartPayload.class),
        SHOW_FIGHT_END(0x22, ShowFightEndPayload.class),
        SHOW_FIGHT_HIT(0x23, ShowFightHitPayload.class),
        SHOW_FIGHT_DEAD(0x24, ShowFightDeadPayload.class),

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

    /**
     * Prepare a buffer and write the header in the reserved space at the
     * beginning.
     *
     * @param dataLen - The size of the data that will fill the buffer.
     * @return The buffer of the correct size and the header filled.
     */
    protected final byte[] prepareBuffer(final int dataLen) {
        final byte[] data = new byte[OFFSET_RESERVED + dataLen];

        // Write opcode
        data[0] = (byte) opcode.getOpcode();

        // Write length
        NetDeSerializer.destructureShort(data, 1, (short) dataLen);

        return data;
    }
}
