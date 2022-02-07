package com.naitcherif.blockchain.services;

import com.naitcherif.blockchain.dtos.request.MineBlockRequestDTO;
import com.naitcherif.blockchain.entities.Block;

import java.util.List;

public interface IBlockchainService {
    List<Block> getChain();

    Block mineBlock(MineBlockRequestDTO data);

    Boolean checkIfChainValid();

    Boolean checkIfChainValid(List<Block> chain);
}
