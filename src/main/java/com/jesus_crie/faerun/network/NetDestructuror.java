package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.*;

import javax.annotation.Nonnull;

/**
 * Utility class that can destructure and restructure primitives and some objects
 * int byte arrays.
 */
public class NetDestructuror {

    public static final int LEN_SHORT = 2;
    public static final int LEN_INT = 4;
    public static final int LEN_LONG = 8;

    /**
     * Concat the 2 bytes after the offset in the given data into a short (16bits).
     *
     * @param data   - The data to read from.
     * @param offset - The offset of the bytes we want to process.
     * @return A short built from the 2 bytes right after the offset.
     */
    public static short rebuildShort(final byte[] data, final int offset) {
        return (short) (data[offset + 1]
                | data[offset] << 8);
    }

    /**
     * Destructure a short by writing each byte into the buffer, starting with the
     * higher bytes.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the write.
     */
    public static int destructureShort(final byte[] data, final int offset, final short value) {
        data[offset] = (byte) (value >> 8);
        data[offset + 1] = (byte) value;

        return offset + LEN_SHORT;
    }

    /**
     * Concat the 4 bytes after the offset in the given data into an integer (32bits).
     *
     * @param data   - The data to read from.
     * @param offset - The offset of the bytes we want to process.
     * @return An int built from the 4 bytes right after the offset.
     */
    public static int rebuildInt(final byte[] data, final int offset) {
        return data[offset + 3]
                | data[offset + 2] << 8
                | data[offset + 1] << 16
                | data[offset] << 24;
    }

    /**
     * Destructure an integer by writing each byte into the buffer, starting with the
     * higher bytes.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the write.
     */
    public static int destructureInt(final byte[] data, final int offset, final int value) {
        data[offset] = (byte) (value >> 24);
        data[offset + 1] = (byte) (value >> 16);
        data[offset + 2] = (byte) (value >> 8);
        data[offset + 3] = (byte) value;

        return offset + LEN_INT;
    }

    /**
     * Concat the 8 bytes after the offset
     * public static final int LEN_LONG = 8; in the given data into a long (64bits).
     *
     * @param data   - The data to read from.
     * @param offset - The offset of the bytes we want to process.
     * @return An int built from the 8 bytes right after the offset.
     */
    public static long rebuildLong(final byte[] data, final int offset) {
        return (long) rebuildInt(data, offset + 4)
                | (long) rebuildInt(data, offset) << 32;
    }

    /**
     * Destructure a long by writing each byte into the buffer, starting with the
     * higher bytes.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the write.
     */
    public static int destructureLong(final byte[] data, final int offset, final long value) {
        destructureInt(data, offset, (int) (value >> 32));
        destructureInt(data, offset + 4, (int) value);

        return offset + LEN_LONG;
    }

    /**
     * Predict the size of a string once destructured.
     *
     * @param str - The value to test.
     * @return The size of the value destructured.
     */
    public static int predictSizeDestructuredString(@Nonnull final String str) {
        return LEN_SHORT + str.length();
    }

    /**
     * Restructure a string from the raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The string contained in the data.
     */
    @Nonnull
    public static String rebuildString(final byte[] data, int offset) {
        // Read len
        final short len = rebuildShort(data, offset);
        offset += 1;

        // Read data
        final byte[] strData = new byte[len];
        System.arraycopy(data, offset, strData, 0, len);

        return new String(strData);
    }

    /**
     * Destructure a string by writing each byte into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset at the end of the writing.
     */
    public static int destructureString(final byte[] data, int offset, @Nonnull final String value) {
        // Length
        final short len = (short) value.length();
        destructureShort(data, offset, len);
        offset += LEN_SHORT;

        // Write data
        final byte[] strData = value.getBytes();
        System.arraycopy(strData, 0, data, offset, len);
        offset += len;

        return offset;
    }

    /**
     * Predict the size of a Player once destructured.
     *
     * @param val - The value to test.
     * @return The size of the value destructured.
     */
    public static int predictSizeDestructuredPlayer(@Nonnull final Player val) {
        return 1 + predictSizeDestructuredString(val.getPseudo());
    }

    /**
     * Restructure a player from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt player.
     */
    public static Player rebuildPlayer(final byte[] data, int offset) {
        // Read side
        final byte rawSide = data[offset];
        final Player.Side side = rawSide == 0 ? Player.Side.LEFT : Player.Side.RIGHT;
        offset += 1;

        // Read name
        final String name = rebuildString(data, offset);

        return new Player(name, side);
    }

    /**
     * Destructure a Player by writing his side and his username into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int destructurePlayer(final byte[] data, int offset, @Nonnull final Player value) {
        // Write side
        if (value.getSide() == Player.Side.LEFT)
            data[offset] = 0;
        else data[offset] = 1;
        offset += 1;

        // Write name length
        final short nameLen = (short) value.getPseudo().length();
        destructureShort(data, offset, nameLen);
        offset += LEN_SHORT;

        // Write name
        final byte[] nameData = value.getPseudo().getBytes();
        System.arraycopy(nameData, 0,
                data, offset, nameLen);
        offset += nameLen;

        return offset;
    }

    public static int predictSizeDestructuredWarrior(@Nonnull final Warrior val) {
        // TODO 2/20/19
        return 0;
    }

    @Nonnull
    public static Warrior rebuildWarrior(final byte[] data, int offset) {
        // TODO 2/20/19
        return null;
    }

    public static int destructureWarrior(final byte[] data, int offset, @Nonnull final Warrior value) {
        // TODO 2/20/19
        return 0;
    }

    /**
     * Restructure a queued warrior from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt warrior.
     */
    @Nonnull
    public static Warrior rebuildWarriorQ(final byte[] data, int offset, @Nonnull final Player owner) {
        final byte type = data[offset];

        switch (type) {
            case 0x00:
                return new Dwarf(owner);
            case 0x01:
                return new DwarfLeader(owner);
            case 0x10:
                return new Elf(owner);
            case 0x11:
                return new ElfLeader(owner);
        }

        throw new MalformedPayloadException("This warriorQ doesn't exist: " + type);
    }

    /**
     * Destructure a queued warrior into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int destructureWarriorQ(final byte[] data, int offset, @Nonnull final Warrior value) {
        final byte type;
        if (value instanceof Dwarf) {
            if (value instanceof DwarfLeader)
                type = 0x00;
            else
                type = 0x01;
        } else if (value instanceof Elf) {
            if (value instanceof ElfLeader)
                type = 0x10;
            else
                type = 0x11;
        } else {
            throw new IllegalArgumentException("This warriorQ can't be destructured: " + value);
        }

        data[offset] = type;

        return offset + 1;
    }

    /**
     * Predict the size of a Castle once destructured.
     *
     * @param val - The value to test.
     * @return The size of the value destructured.
     */
    public static int predictSizeDestructuredCastle(@Nonnull final Castle val) {
        return predictSizeDestructuredPlayer(val.getOwner())
                + LEN_SHORT + 1 + LEN_SHORT + val.getTrainingQueue().size();
    }

    /**
     * Restructure a Castle from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt castle.
     */
    public static Castle rebuildCastle(final byte[] data, int offset) {
        // Read owner
        final Player owner = rebuildPlayer(data, offset);
        offset += predictSizeDestructuredPlayer(owner);

        // Read resources
        final short resources = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read base cost
        final byte baseCost = data[offset];
        offset += 1;

        // Rebuild castle
        final Castle castle = new Castle(owner, baseCost, resources);

        // Read queue
        final short size = rebuildShort(data, offset);
        offset += LEN_SHORT;

        if (size > 0) {
            final Warrior[] queue = new Warrior[size];
            for (int i = 0; i < size; i++) {
                queue[i] = rebuildWarriorQ(data, offset, owner);
                offset += 1;

            }

            castle.queueWarriors(queue);
        }

        return castle;
    }

    /**
     * Destructure a Castle by writing his owner, resources and trainingQueue into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int destructureCastle(final byte[] data, int offset, @Nonnull final Castle value) {
        // Write owner
        offset = destructurePlayer(data, offset, value.getOwner());

        // Write resources
        final short resources = (short) value.getResources();
        offset = destructureShort(data, offset, resources);

        // Write base cost
        data[offset] = (byte) value.getBaseCost();
        offset += 1;

        // Write queue
        final short size = (short) value.getTrainingQueue().size();
        offset = destructureShort(data, offset, size);

        if (size > 0) {
            for (Warrior w : value.getTrainingQueue()) {
                offset = destructureWarriorQ(data, offset, w);
            }
        }

        return offset;
    }
}
