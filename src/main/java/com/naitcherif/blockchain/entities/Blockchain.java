package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;

import java.time.Instant;
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

    public static void addBlock(String data) {
        if (data == null)
            throw new IllegalArgumentException("Data can't be null");
        List<Block> chain = instance.chain;
        var lastBlock = chain.get(chain.size() - 1);
        chain.add(new Block(Instant.now(), lastBlock, data));
    }

    public static List<Block> getChain() {
        return getInstance().chain;
    }

    public static boolean isChainValid(List<Block> chain) {
        // check if the first block is the genesis block
        if (Block.genesisBlock().equals(chain.get(0))) {
            // check if there are other blocks then validate them
            return validateBlocks(chain);
        }
        return false;
    }

    private static boolean validateBlocks(List<Block> chain) {
        if (chain.size() > 1) {
            for (int i = 1; i < chain.size(); i++) {
                var previousBlock = chain.get(i - 1);
                Block currentBlock = chain.get(i);
                if (!currentBlock.lastHash().equals(previousBlock.hash()))
                    return false;
                var calculatedHash = ShaUtils.digest(previousBlock);
                if (!currentBlock.hash().equals(calculatedHash))
                    return false;
            }
            return true;
        }
        return true;
    }
}
