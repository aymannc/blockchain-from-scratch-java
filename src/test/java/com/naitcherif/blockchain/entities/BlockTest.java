package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.naitcherif.blockchain.entities.Blockchain.testDifficulty;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Blocks")
class BlockTest {

    @Test
    @DisplayName("Testing the genesis block initialization")
    void genesisBlock() {
        var currentInstant = Instant.now();
        var genesisBlock = Block.genesisBlock();
        assertAll(
                () -> assertNotNull(genesisBlock),
                () -> assertTrue(currentInstant.compareTo(genesisBlock.getTimestamp()) >= 0),
                () -> assertNull(genesisBlock.getLastHash())
        );
    }

    @Test
    @DisplayName("Testing mining new blocks")
    void mineBlock() {
        var instant = Instant.now();
        var genesisBlock = Block.genesisBlock();
        var minedData = "Mined Data";
        var minedBlock = Block.mineBlock(genesisBlock, minedData, testDifficulty);
        assertAll(
                () -> assertNotNull(genesisBlock),
                () -> assertEquals(genesisBlock.getHash(), minedBlock.getLastHash()),
                () -> assertTrue(instant.compareTo(minedBlock.getTimestamp()) <= 0),
                () -> assertFalse(minedBlock.getHash().isEmpty()),
                () -> assertTrue(minedBlock.getHash().startsWith("0".repeat(testDifficulty)))
        );
    }
}