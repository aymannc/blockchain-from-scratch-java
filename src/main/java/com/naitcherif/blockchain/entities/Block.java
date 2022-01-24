package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Block {
    private final Instant timestamp;
    private final String lastHash;
    private final int difficulty;
    private final int nones;
    private String data;
    private String hash;

    public static Block mineBlock(Block lastBlock, String data) {
        return mineBlock(lastBlock, data, Blockchain.DIFFICULTY);
    }

    public static Block mineBlock(Block lastBlock, String data, int difficulty) {
        Instant timestamp;
        var nones = 0;
        String hash;
        String difficultyString = "0".repeat(difficulty);
        do {
            timestamp = Instant.now();
            nones++;
            hash = ShaUtils.digest(timestamp, lastBlock.hash, data, difficulty, nones);
        } while (!hash.startsWith(difficultyString));
        Blockchain.updateDifficulty(lastBlock, timestamp);
        return new Block(timestamp, lastBlock.getHash(), difficulty, nones, data, hash);
    }

    public static Block genesisBlock() {
        String data = "genesis block";
        Block block = new Block(Instant.MIN,
                null,
                0,
                0,
                data,
                null
        );
        block.setHash(ShaUtils.digest(block));
        return block;
    }
}
