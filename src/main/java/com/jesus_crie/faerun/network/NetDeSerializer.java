package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.logic.BoardLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that can destructure and restructure primitives and some objects
 * int byte arrays.
 */
public class NetDeSerializer {

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
     * Predict the size of a string once serialized.
     *
     * @param str - The value to test.
     * @return The size of the value destructured.
     */
    public static int predictSizeSerializedString(@Nonnull final String str) {
        return LEN_SHORT + str.length();
    }

    /**
     * Deserialize a string from the raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The string contained in the data.
     */
    @Nonnull
    public static String deserializeString(final byte[] data, int offset) {
        // Read len
        final short len = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read data
        final byte[] strData = new byte[len];
        System.arraycopy(data, offset, strData, 0, len);

        return new String(strData);
    }

    /**
     * Serialize a string by writing each byte into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset at the end of the writing.
     */
    public static int serializeString(final byte[] data, int offset, @Nonnull final String value) {
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
     * Predict the size of a Player once serialized.
     *
     * @param val - The value to test.
     * @return The size of the value serialized.
     */
    public static int predictSizeSerializedPlayer(@Nonnull final Player val) {
        return 1 + predictSizeSerializedString(val.getPseudo());
    }

    /**
     * Deserialize a player from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt player.
     */
    public static Player deserializePlayer(final byte[] data, int offset) {
        // Read side
        final byte rawSide = data[offset];
        final Player.Side side = rawSide == 0 ? Player.Side.LEFT : Player.Side.RIGHT;
        offset += 1;

        // Read name
        final String name = deserializeString(data, offset);

        return new Player(name, side);
    }

    /**
     * Serialize a Player by writing his side and his username into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializePlayer(final byte[] data, int offset, @Nonnull final Player value) {
        // Write side
        if (value.getSide() == Player.Side.LEFT)
            data[offset] = 0;
        else data[offset] = 1;
        offset += 1;

        // Write name
        offset += serializeString(data, offset, value.getPseudo());

        return offset;
    }

    /**
     * Predict the size of a Warrior once serialized.
     *
     * @param val - The value to test.
     * @return The size of the value serialized.
     */
    public static int predictSizeSerializedWarrior(@Nonnull final Warrior val) {
        return predictSizeSerializedPlayer(val.getOwner()) + 1 + LEN_INT;
    }

    /**
     * Deserialize a Warrior from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @param <T>    - The type of warrior to deserialize.
     * @return The rebuilt Warrior.
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends Warrior> T deserializeWarrior(final byte[] data, int offset) {
        // Read owner
        final Player owner = deserializePlayer(data, offset);
        offset += predictSizeSerializedPlayer(owner);

        // Read type and build object
        final Warrior warrior = deserializeWarriorType(data, offset, owner);
        offset += 1;

        // Read health
        final int health = rebuildInt(data, offset);
        warrior.setHealth(health);

        return (T) warrior;
    }

    /**
     * Serialize a Warrior by writing his type, health and owner into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializeWarrior(final byte[] data, int offset, @Nonnull final Warrior value) {
        // Write owner
        offset += serializePlayer(data, offset, value.getOwner());

        // Write type
        offset += serializeWarriorType(data, offset, value);

        // Write health
        offset += destructureInt(data, offset, value.getHealth());

        return offset;
    }

    /**
     * Predict the size of a Warrior company once serialized.
     *
     * @param val - The value to test.
     * @return The size of the value serialized.
     */
    public static int predictSizeSerializedWarriorCompany(@Nonnull final List<Warrior> val) {
        if (val.isEmpty())
            return LEN_SHORT;
        return LEN_SHORT + predictSizeSerializedPlayer(val.get(0).getOwner())
                + val.size() * (1 + LEN_INT);
    }

    /**
     * Deserialize a Warrior company from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @param <T>    - The type of warrior to deserialize.
     * @return The rebuilt Warrior.
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends Warrior> List<T> deserializeWarriorCompany(final byte[] data, int offset) {
        // Read len
        final short len = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read owner
        final Player owner = deserializePlayer(data, offset);
        offset += predictSizeSerializedPlayer(owner);

        final List<T> company = new ArrayList<>(len);

        // Read each warrior
        for (int i = 0; i < len; i++) {
            final Warrior w = deserializeWarriorType(data, offset, owner);
            offset += 1;

            final int health = rebuildInt(data, offset);
            offset += LEN_INT;

            company.add((T) w);
        }

        return company;
    }

    /**
     * Serialize a Warrior company by writing his owner and for each it's type and health into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializeWarriorCompany(final byte[] data, int offset, @Nonnull final List<Warrior> value) {
        // Write len
        offset += destructureShort(data, offset, (short) value.size());

        if (!value.isEmpty()) {
            // Write owner
            final Player owner = value.get(0).getOwner();
            offset += serializePlayer(data, offset, owner);

            for (final Warrior w : value) {
                // Write type
                offset += serializeWarriorType(data, offset, w);

                // Write health
                offset += destructureInt(data, offset, w.getHealth());
            }
        }

        return offset;
    }

    /**
     * Deserialize a warrior type from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt warrior.
     */
    @Nonnull
    public static Warrior deserializeWarriorType(final byte[] data, int offset, @Nonnull final Player owner) {
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
     * Serialize a warrior type into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializeWarriorType(final byte[] data, int offset, @Nonnull final Warrior value) {
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
     * Predict the size of a Castle once serialized.
     *
     * @param val - The value to test.
     * @return The size of the value serialized.
     */
    public static int predictSizeSerializedCastle(@Nonnull final Castle val) {
        return predictSizeSerializedPlayer(val.getOwner())
                + LEN_SHORT + 1 + LEN_SHORT + val.getTrainingQueue().size();
    }

    /**
     * Deserialize a Castle from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt castle.
     */
    public static Castle deserializeCastle(final byte[] data, int offset) {
        // Read owner
        final Player owner = deserializePlayer(data, offset);
        offset += predictSizeSerializedPlayer(owner);

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
                queue[i] = deserializeWarriorType(data, offset, owner);
                offset += 1;

            }

            castle.queueWarriors(queue);
        }

        return castle;
    }

    /**
     * Serialize a Castle by writing his owner, resources and trainingQueue into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializeCastle(final byte[] data, int offset, @Nonnull final Castle value) {
        // Write owner
        offset = serializePlayer(data, offset, value.getOwner());

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
                offset = serializeWarriorType(data, offset, w);
            }
        }

        return offset;
    }

    /**
     * Predict the size of the settings once serialized.
     *
     * @param val - The value to test.
     * @return The size of the value serialized.
     */
    public static int predictSizeSerializedSettings(@Nonnull final BoardSettings val) {
        return LEN_SHORT * 5;
    }

    /**
     * Deserialize the settings from raw bytes.
     *
     * @param data   - The data to read from.
     * @param offset - The offset in the data where we will read.
     * @return The rebuilt settings.
     */
    @Nonnull
    public static BoardSettings deserializeSettings(final byte[] data, int offset) {
        // Read size
        final short size = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read base cost
        final short baseCost = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read diceAmount
        final short diceAmount = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read initialResources
        final short initialResources = rebuildShort(data, offset);
        offset += LEN_SHORT;

        // Read resources/round
        final short resourcesPerRound = rebuildShort(data, offset);
        offset += LEN_SHORT;

        return new BoardSettings(size, baseCost, diceAmount, initialResources, resourcesPerRound);
    }

    /**
     * Serialize the settings by writing his fields into the data.
     *
     * @param data   - The data to write to.
     * @param offset - The offset in the data where we will write.
     * @param value  - The value to write.
     * @return The offset to the end of the thing.
     */
    public static int serializeSettings(final byte[] data, int offset, @Nonnull final BoardSettings value) {
        // Write size
        offset += value.getSize();

        // Write base cost
        offset += value.getBaseCost();

        // Write diceAmount
        offset += value.getDiceAmount();

        // Write initialResources
        offset += value.getInitialResources();

        // Write ressources/round
        offset += value.getResourcesPerRound();

        return offset;
    }
}
