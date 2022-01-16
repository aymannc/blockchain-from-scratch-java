package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Blockchain")
class BlockchainTest {

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
                () -> assertEquals(latestBlock.data(), data),
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
        Block invalidBlock = new Block(Instant.MIN, "Invalid-hash", "hello");
        Block firstBlock = new Block(Instant.now(), Block.genesisBlock(), "Block 1");
        Block secondBlock = new Block(Instant.now(), firstBlock, "Block 2");
        Block thirdBlock = new Block(Instant.now(), invalidBlock, "Block 3");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, secondBlock, thirdBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }

    @Test()
    @DisplayName("A chain is invalid when one of its attributes is invalid")
    void checkBlockchainBlockLastAttributesShouldReturnFalse() {
        Block firstBlock = new Block(Instant.now(), Block.genesisBlock(), "Block 1");
        Block secondBlock = new Block(Instant.now(), firstBlock, "Block 2");
        Block invalidBlock = new Block(Instant.now(), firstBlock, "Block 2-changed");
        Block thirdBlock = new Block(Instant.now(), secondBlock, "Block 3");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, invalidBlock, thirdBlock);
        assertFalse(Blockchain.isChainValid(chain));
    }


    @Test()
    @DisplayName("A chain is invalid when all of its blocks are valid")
    void checkBlockchainValidityShouldReturnTrue() {
        Block firstBlock = new Block(Instant.now(), Block.genesisBlock(), "Block 1");
        Block secondBlock = new Block(Instant.now(), firstBlock, "Block 2");
        Block thirdBlock = new Block(Instant.now(), secondBlock, "Block 3");
        List<Block> chain = List.of(Block.genesisBlock(), firstBlock, secondBlock, thirdBlock);
        assertTrue(Blockchain.isChainValid(chain));
    }
}