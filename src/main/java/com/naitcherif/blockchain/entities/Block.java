package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;

import java.time.Instant;

public record Block(Instant timestamp, String lastHash, String hash, String data) {
    public Block(String lastHash, String data) {
        this(Instant.now(), lastHash, ShaUtils.digest(data), data);
    }

    public static Block genesisBlock() {
        return new Block(Instant.MIN, null, ShaUtils.digest(""), "");
    }
}
