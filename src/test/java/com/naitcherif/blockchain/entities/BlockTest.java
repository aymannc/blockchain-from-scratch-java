package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
                () -> assertTrue(currentInstant.compareTo(genesisBlock.timestamp()) >= 0),
                () -> assertTrue(genesisBlock.data().isEmpty()),
                () -> assertNull(genesisBlock.lastHash())
        );
    }

    @Test
    @DisplayName("Testing mining new blocks")
    void mineBlock() {
        var instant = Instant.now();
        var genesisBlock = Block.genesisBlock();
        var minedData = "Mined Data";
        var minedBlock = new Block(Instant.now(),genesisBlock, minedData);
        assertAll(
                () -> assertNotNull(genesisBlock),
                () -> assertEquals(genesisBlock.hash(), minedBlock.lastHash()),
                () -> assertTrue(instant.compareTo(minedBlock.timestamp()) <= 0),
                () -> assertFalse(minedBlock.hash().isEmpty())
        );
    }
}