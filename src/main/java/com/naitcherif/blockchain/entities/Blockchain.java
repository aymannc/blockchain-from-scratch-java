package com.naitcherif.blockchain.entities;

import java.util.ArrayList;
import java.util.List;

public record Blockchain(List<Block> chain) {
    private static final Blockchain instance = new Blockchain();

    public Blockchain() {
        this(new ArrayList<>(List.of(Block.genesisBlock())));
    }

    public static Blockchain getInstance() {
        return instance;
    }

    public static Block getLatestBlock() {
        List<Block> chain = instance.chain;
        return chain.get(chain.size() - 1);
    }

    public static void addBlock(Block block) {
        instance.chain.add(block);
    }

}
