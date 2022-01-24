package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record Blockchain(List<Block> chain) {
    public static int STARTING_DIFFICULTY = 3;
    public static int DIFFICULTY = 3;
    public static int TEST_DIFFICULTY = 1;
    public static int MINE_RATE = 1000;
    private static Blockchain INSTANCE = new Blockchain();

    public Blockchain() {
        this(new ArrayList<>(List.of(Block.genesisBlock())));
    }

    public static Blockchain getInstance() {
        return INSTANCE;
    }

    public static void resetInstance() {
        INSTANCE = new Blockchain();
        DIFFICULTY = Blockchain.STARTING_DIFFICULTY;
    }

    public static Block getLatestBlock() {
        List<Block> chain = INSTANCE.chain;
        return chain.get(chain.size() - 1);
    }

    public static void addBlock(String data) {
        if (data == null)
            throw new IllegalArgumentException("Data can't be null");
        List<Block> chain = INSTANCE.chain;
        var lastBlock = chain.get(chain.size() - 1);
        chain.add(Block.mineBlock(lastBlock, data));
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
                if (!currentBlock.getLastHash().equals(previousBlock.getHash()))
                    return false;
                var calculatedHash = ShaUtils.digest(currentBlock);
                if (!currentBlock.getHash().equals(calculatedHash))
                    return false;
            }
            return true;
        }
        return true;
    }

    public static void replaceChain(List<Block> chain) {
        if (INSTANCE.chain.size() < chain.size() && isChainValid(chain)) {
            INSTANCE = new Blockchain(chain);
        } else
            throw new IllegalArgumentException("The new chain is smaller than the old chain or it's invalid!");
    }

    public static void updateDifficulty(Block previousBlock, Instant instant) {
        long timestamp = previousBlock.getTimestamp() == Instant.MIN ? 0 : previousBlock.getTimestamp().toEpochMilli();
        if (instant.toEpochMilli() - timestamp > MINE_RATE) {
            DIFFICULTY--;
            if (DIFFICULTY < 1)
                DIFFICULTY = 1;
        } else
            DIFFICULTY++;
    }
}
