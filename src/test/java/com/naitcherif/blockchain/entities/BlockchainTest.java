package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Testing the blockchain block add")
    void shouldAddBlock() {
        var block = new Block(Block.genesisBlock(), "Second block");
        Blockchain.addBlock(block);
        Block latestBlock = Blockchain.getLatestBlock();
        assertAll(
                () -> assertNotNull(latestBlock),
                () -> assertEquals(latestBlock, block)
        );
    }
}