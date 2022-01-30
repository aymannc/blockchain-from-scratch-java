package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Blockchain")
class BlockchainTest {

    private final int TEST_DIFFICULTY = 1;
    Blockchain blockchain;

    @BeforeEach
    void resetInstance() {
        blockchain = new Blockchain();
        Blockchain.resetInstance();
    }

    @Test
    @DisplayName("Testing the blockchain initialization")
    void blockchainInitialization() {
        var genesisBlock = Block.genesisBlock();
        assertAll(
                () -> assertNotNull(blockchain),
                () -> assertNotNull(genesisBlock),
                () -> assertNotNull(blockchain.getChain()),
                () -> assertFalse(blockchain.getChain().isEmpty()),
                () -> assertEquals(blockchain.getChain().get(0), genesisBlock)
        );
    }

    @Test
    @DisplayName("should add a block to the blockchain")
    void shouldAddBlock() {
        String data = "Second block";
        Block latestBlock = blockchain.mineBlock(data);
        assertAll(
                () -> assertNotNull(latestBlock),
                () -> assertEquals(latestBlock.getData(), data),
                () -> assertEquals(2, blockchain.getChain().size())
        );
    }

    @Test
    @DisplayName("should throw exception on block add")
    void shouldThrowExceptionAddBlock() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> blockchain.mineBlock(null)
        );

        assertTrue(thrown.getMessage().contains("null"));
    }

    @Test()
    @DisplayName("A chain is invalid when it does not start with the genesis block")
    void checkBlockchainStartsWithTheGenesisBlockCaseShouldReturnFalse() {
        List<Block> chain = blockchain.getChain();
        chain.set(0, null);
        assertFalse(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("A chain is invalid when one of its block's lastHash is invalid")
    void checkBlockchainBlockLastHashCaseShouldReturnFalse() {
        Block invalidBlock = Block.mineBlock(Block.genesisBlock(), "invalidBlock", TEST_DIFFICULTY);
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "Block 1", TEST_DIFFICULTY);
        Block secondBlock = Block.mineBlock(firstBlock, "Block 2", TEST_DIFFICULTY);
        invalidBlock.setHash("invalid hash");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, invalidBlock, secondBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("A chain is invalid when one of its attributes is invalid")
    void checkBlockchainBlockLastAttributesShouldReturnFalse() {
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "", TEST_DIFFICULTY);
        Block secondBlock = Block.mineBlock(firstBlock, "Block 2", TEST_DIFFICULTY);
        Block thirdBlock = Block.mineBlock(secondBlock, "Block 3", TEST_DIFFICULTY);
        secondBlock.setData("Block 2 changed");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, thirdBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }


    @Test()
    @DisplayName("A chain is valid when all of its blocks are valid")
    void checkBlockchainValidityShouldReturnTrue() {
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "firstBlock", TEST_DIFFICULTY);
        Block secondBlock = Block.mineBlock(firstBlock, "secondBlock", TEST_DIFFICULTY);
        Block thirdBlock = Block.mineBlock(secondBlock, "thirdBlock", TEST_DIFFICULTY);
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, secondBlock, thirdBlock);
        assertTrue(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("should throw exception on chain length")
    void shouldThrowExceptionOnReplaceChainLength() {
        List<Block> newChain = List.of(Block.genesisBlock());
        blockchain.mineBlock("Block 1");
        blockchain.mineBlock("Block 2");
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> blockchain.replaceChain(newChain)
        );
        assertTrue(thrown.getMessage().contains("smaller"));
    }

    @Test()
    @DisplayName("should throw exception on chain validity")
    void shouldThrowExceptionOnReplaceChainValidity() {
        Block block = Block.mineBlock(Block.genesisBlock(), "Block 0", TEST_DIFFICULTY);
        block.setData("Block 1");
        List<Block> newChain = List.of(
                Block.genesisBlock(),
                block
        );
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> blockchain.replaceChain(newChain)
        );
        assertTrue(thrown.getMessage().contains("invalid"));
    }

    @Test()
    @DisplayName("should replace chain")
    void shouldReplaceChain() {
        var oldChain = blockchain.getChain();
        Block block1 = Block.mineBlock(Block.genesisBlock(), "Block 1", TEST_DIFFICULTY);
        Block block2 = Block.mineBlock(block1, "Block 2", TEST_DIFFICULTY);
        List<Block> newChain = List.of(Block.genesisBlock(), block1, block2);
        blockchain.replaceChain(newChain);
        assertAll(
                () -> assertNotEquals(blockchain.getChain(), oldChain),
                () -> assertEquals(blockchain.getChain(), newChain)
        );
    }

    @Test()
    @DisplayName("should increase difficulty")
    void shouldIncreaseDifficulty() {
        Block block = Block.mineBlock(Block.genesisBlock(), "Block 1", TEST_DIFFICULTY);
        var oldDifficulty = Blockchain.getDifficulty();
        Blockchain.updateDifficulty(block, Instant.ofEpochMilli(block.getTimestamp().toEpochMilli() + Blockchain.MINE_RATE - 100));
        assertEquals(Blockchain.getDifficulty(), oldDifficulty + 1);
    }

    @Test()
    @DisplayName("should decrease difficulty")
    void shouldDecreaseDifficulty() {
        var oldDifficulty = Blockchain.getDifficulty();
        Block block = Block.mineBlock(Block.genesisBlock(), "Block 1", TEST_DIFFICULTY);
        Blockchain.updateDifficulty(block, Instant.ofEpochMilli(block.getTimestamp().toEpochMilli() + Blockchain.MINE_RATE + 200));
        assertTrue(Blockchain.getDifficulty() <= oldDifficulty - 1);
    }

    @Test()
    @DisplayName("should update difficulty while mining a new block")
    void shouldIncreaseDifficultyWhileMiningANewBlock() {
        Block.mineBlock(Block.genesisBlock(), "Hello", 0);
        assertEquals(2, Blockchain.getDifficulty());
    }
}