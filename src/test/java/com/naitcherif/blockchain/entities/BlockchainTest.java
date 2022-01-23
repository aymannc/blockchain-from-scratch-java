package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.naitcherif.blockchain.entities.Blockchain.testDifficulty;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Blockchain")
class BlockchainTest {

    @BeforeEach
    void resetInstance() {
        Blockchain.resetInstance();
    }

    @Test
    @DisplayName("Testing the blockchain initialization")
    void blockchainInitialization() {
        var currentInstant = Blockchain.getInstance();
        var genesisBlock = Block.genesisBlock();
        assertAll(
                () -> assertNotNull(currentInstant),
                () -> assertNotNull(genesisBlock),
                () -> assertNotNull(currentInstant.chain()),
                () -> assertFalse(currentInstant.chain().isEmpty()),
                () -> assertEquals(currentInstant.chain().get(0), genesisBlock)
        );
    }

    @Test
    @DisplayName("should add a block to the blockchain")
    void shouldAddBlock() {
        String data = "Second block";
        Blockchain.addBlock(data);
        Block latestBlock = Blockchain.getLatestBlock();
        assertAll(
                () -> assertNotNull(latestBlock),
                () -> assertEquals(latestBlock.getData(), data),
                () -> assertEquals(2, Blockchain.getChain().size())
        );
    }

    @Test
    @DisplayName("should throw exception on block add")
    void shouldThrowExceptionAddBlock() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> Blockchain.addBlock(null)
        );

        assertTrue(thrown.getMessage().contains("null"));
    }

    @Test()
    @DisplayName("A chain is invalid when it does not start with the genesis block")
    void checkBlockchainStartsWithTheGenesisBlockCaseShouldReturnFalse() {
        List<Block> chain = Blockchain.getChain();
        chain.set(0, null);
        assertFalse(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("A chain is invalid when one of its block's lastHash is invalid")
    void checkBlockchainBlockLastHashCaseShouldReturnFalse() {
        Block invalidBlock = Block.mineBlock(Block.genesisBlock(), "invalidBlock", testDifficulty);
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "Block 1", testDifficulty);
        Block secondBlock = Block.mineBlock(firstBlock, "Block 2", testDifficulty);
        invalidBlock.setHash("invalid hash");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, invalidBlock, secondBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("A chain is invalid when one of its attributes is invalid")
    void checkBlockchainBlockLastAttributesShouldReturnFalse() {
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "", testDifficulty);
        Block secondBlock = Block.mineBlock(firstBlock, "Block 2", testDifficulty);
        Block thirdBlock = Block.mineBlock(secondBlock, "Block 3", testDifficulty);
        secondBlock.setData("Block 2 changed");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, thirdBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }


    @Test()
    @DisplayName("A chain is valid when all of its blocks are valid")
    void checkBlockchainValidityShouldReturnTrue() {
        Block firstBlock = Block.mineBlock(Block.genesisBlock(), "firstBlock", testDifficulty);
        Block secondBlock = Block.mineBlock(firstBlock, "secondBlock", testDifficulty);
        Block thirdBlock = Block.mineBlock(secondBlock, "thirdBlock", testDifficulty);
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, secondBlock, thirdBlock);
        assertTrue(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("should throw exception on chain length")
    void shouldThrowExceptionOnReplaceChainLength() {
        List<Block> newChain = List.of(Block.genesisBlock());
        Blockchain.addBlock("Block 1");
        Blockchain.addBlock("Block 2");
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> Blockchain.replaceChain(newChain)
        );
        assertTrue(thrown.getMessage().contains("smaller"));
    }

    @Test()
    @DisplayName("should throw exception on chain validity")
    void shouldThrowExceptionOnReplaceChainValidity() {
        Block block = Block.mineBlock(Block.genesisBlock(), "Block 0", testDifficulty);
        block.setData("Block 1");
        List<Block> newChain = List.of(
                Block.genesisBlock(),
                block
        );
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> Blockchain.replaceChain(newChain)
        );
        assertTrue(thrown.getMessage().contains("invalid"));
    }

    @Test()
    @DisplayName("should replace chain")
    void shouldReplaceChain() {
        var oldChain = Blockchain.getChain();
        Block block1 = Block.mineBlock(Block.genesisBlock(), "Block 1", testDifficulty);
        Block block2 = Block.mineBlock(block1, "Block 2", testDifficulty);
        List<Block> newChain = List.of(Block.genesisBlock(), block1, block2);
        Blockchain.replaceChain(newChain);
        assertAll(
                () -> assertNotEquals(Blockchain.getChain(), oldChain),
                () -> assertEquals(Blockchain.getChain(), newChain)
        );
    }
}