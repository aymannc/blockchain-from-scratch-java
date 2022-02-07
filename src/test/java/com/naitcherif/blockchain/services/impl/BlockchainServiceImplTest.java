package com.naitcherif.blockchain.services.impl;

import com.naitcherif.blockchain.dtos.request.MineBlockRequestDTO;
import com.naitcherif.blockchain.entities.Block;
import com.naitcherif.blockchain.entities.Blockchain;
import com.naitcherif.blockchain.services.IBlockchainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainServiceImplTest {

    Blockchain blockchain;
    IBlockchainService blockchainService;

    @BeforeEach
    void setUp() {
        blockchain = new Blockchain();
        blockchainService = new BlockchainServiceImpl(blockchain);
    }

    @Test
    void getChain() {
        List<Block> chain = blockchainService.getChain();
        assertAll(
                () -> assertNotNull(chain),
                () -> assertTrue(chain.size() > 0),
                () -> assertTrue(Blockchain.isChainValid(chain))
        );
    }

    @Test
    void mineBlock() {
        String data = "hello";
        MineBlockRequestDTO dto = new MineBlockRequestDTO(data);
        Block block = blockchainService.mineBlock(dto);
        assertAll(
                () -> assertNotNull(block),
                () -> assertEquals(data, block.getData())
        );
    }

    @Test
    void checkIfChainValid() {
        MineBlockRequestDTO dto = new MineBlockRequestDTO("hello");
        Block block = blockchainService.mineBlock(dto);
        List<Block> chain = blockchainService.getChain();
        assertAll(
                () -> assertNotNull(block),
                () -> assertNotNull(chain),
                () -> assertTrue(blockchainService.checkIfChainValid(chain))
        );
    }
}