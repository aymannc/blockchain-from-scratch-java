package com.naitcherif.blockchain.entities;

import com.naitcherif.blockchain.utils.ShaUtils;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class Blockchain {

    public static final int STARTING_DIFFICULTY = 3;
    public static final int MINE_RATE = 6000;
    private static int difficulty = 3;

    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>(List.of(Block.genesisBlock()));
    }

    public static int getDifficulty() {
        return difficulty;
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

    public static void resetInstance() {
        difficulty = STARTING_DIFFICULTY;
    }

    public static synchronized void updateDifficulty(Block previousBlock, Instant instant) {
        long timestamp = previousBlock.getTimestamp() == Instant.MIN ? 0 : previousBlock.getTimestamp().toEpochMilli();
        if (instant.toEpochMilli() - timestamp > MINE_RATE) {
            difficulty--;
            if (difficulty < 1)
                difficulty = 1;
        } else
            difficulty++;
    }

    public synchronized Block addBlock(String data) {
        if (data == null)
            throw new IllegalArgumentException("Data can't be null");
        var lastBlock = chain.get(chain.size() - 1);
        Block block = Block.mineBlock(lastBlock, data);
        chain.add(block);
        return block;
    }

    public synchronized void replaceChain(List<Block> newChain) {
        if (chain.size() < newChain.size() && isChainValid(newChain)) {
            chain = newChain;
        } else
            throw new IllegalArgumentException("The new chain is smaller than the old chain or it's invalid!");
    }
}
