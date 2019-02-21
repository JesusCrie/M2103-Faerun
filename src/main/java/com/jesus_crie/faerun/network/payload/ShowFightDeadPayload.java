package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.network.NetDeSerializer;

import javax.annotation.Nonnull;

/**
 * Send a dead warrior to the remote client and ask him to display it.
 */
public class ShowFightDeadPayload extends NetPayload {

    private final Warrior dead;

    public ShowFightDeadPayload(@Nonnull final Warrior dead) {
        super(Opcode.SHOW_FIGHT_DEAD);
        this.dead = dead;
    }

    public ShowFightDeadPayload(final byte[] data) {
        super(Opcode.SHOW_FIGHT_DEAD);
        dead = NetDeSerializer.deserializeWarrior(data, OFFSET_RESERVED);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(NetDeSerializer.predictSizeSerializedWarrior(dead));
        NetDeSerializer.serializeWarrior(data, OFFSET_RESERVED, dead);

        return data;
    }
}
