package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;

import java.time.Instant;

public record Block(Instant timestamp, String lastHash, String hash, String data) {
    public Block(Instant instant, Block lastBlock, String data) {
        this(instant, lastBlock.hash, ShaUtils.digest(lastBlock), data);
    }

    Block(Instant timestamp, String lastHash, String data) {
        this(timestamp, lastHash, ShaUtils.digest(data), data);
    }

    public static Block genesisBlock() {
        String data = "genesis block";
        return new Block(Instant.MIN, null, ShaUtils.digest(data), data);
    }
}
