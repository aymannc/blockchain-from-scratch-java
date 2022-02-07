package com.naitcherif.blockchain.services.impl;

import com.naitcherif.blockchain.dtos.request.MineBlockRequestDTO;
import com.naitcherif.blockchain.entities.Block;
import com.naitcherif.blockchain.entities.Blockchain;
import com.naitcherif.blockchain.services.IBlockchainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockchainServiceImpl implements IBlockchainService {

    private final Blockchain blockchain;

    @Override
    public List<Block> getChain() {
        return blockchain.getChain();
    }

    @Override
    public Block mineBlock(MineBlockRequestDTO data) {
        return blockchain.mineBlock(data.data());
    }

    @Override
    public Boolean checkIfChainValid() {
        return Blockchain.isChainValid(blockchain.getChain());
    }

    @Override
    public Boolean checkIfChainValid(List<Block> chain) {
        return Blockchain.isChainValid(chain);
    }
}
