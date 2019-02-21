package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.network.NetDeSerializer;

import javax.annotation.Nonnull;

/**
 * Send a fight step to the remote client and ask him to display it.
 */
public class ShowFightHitPayload extends NetPayload {

    private final Warrior rhs;
    private final Warrior lhs;
    private final int damage;

    public ShowFightHitPayload(@Nonnull final Warrior rhs,
                               @Nonnull final Warrior lhs,
                               final int damage) {
        super(Opcode.SHOW_FIGHT_HIT);
        this.rhs = rhs;
        this.lhs = lhs;
        this.damage = damage;
    }

    public ShowFightHitPayload(final byte[] data) {
        super(Opcode.SHOW_FIGHT_HIT);
        int offset = OFFSET_RESERVED;

        rhs = NetDeSerializer.deserializeWarrior(data, offset);
        offset += NetDeSerializer.predictSizeSerializedWarrior(rhs);
        lhs = NetDeSerializer.deserializeWarrior(data, offset);
        offset += NetDeSerializer.predictSizeSerializedWarrior(lhs);

        damage = NetDeSerializer.rebuildInt(data, offset);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(
                NetDeSerializer.predictSizeSerializedWarrior(rhs)
                        + NetDeSerializer.predictSizeSerializedWarrior(lhs)
                        + NetDeSerializer.LEN_INT
        );

        int offset = OFFSET_RESERVED;

        offset += NetDeSerializer.serializeWarrior(data, offset, rhs);
        offset += NetDeSerializer.serializeWarrior(data, offset, lhs);
        offset += NetDeSerializer.destructureInt(data, offset, damage);

        return data;
    }
}
