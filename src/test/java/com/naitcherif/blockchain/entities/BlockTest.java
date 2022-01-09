package com.naitcherif.blockchain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Blocks")
class BlockTest {

    @Test
    @DisplayName("Testing Blocks initialization")
    void initBlock() {
        var instant = Instant.now();
        Block block = new Block("", "Hello");
        assertAll(
                () -> assertNotNull(block),
                () -> assertTrue(instant.compareTo(block.timestamp()) <= 0),
                () -> assertFalse(block.hash().isEmpty())
        );
    }


}