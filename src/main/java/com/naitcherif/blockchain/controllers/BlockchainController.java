package com.naitcherif.blockchain.controllers;

import com.naitcherif.blockchain.dtos.request.MineBlockRequestDTO;
import com.naitcherif.blockchain.entities.Block;
import com.naitcherif.blockchain.services.IBlockchainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.endpoint}/blocks")
public class BlockchainController {
    private final IBlockchainService blockchainService;

    @GetMapping
    public ResponseEntity<List<Block>> getBlocks() {
        return ResponseEntity.ok(blockchainService.getChain());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Block> mineBlock(@Valid @RequestBody MineBlockRequestDTO blockRequest) {
        return ResponseEntity.ok(blockchainService.mineBlock(blockRequest));
    }

    @GetMapping(path = "validate")
    public ResponseEntity<Boolean> checkIfLocalChainValid() {
        return ResponseEntity.ok(blockchainService.checkIfChainValid());
    }

    @PostMapping(path = "validate")
    public ResponseEntity<Boolean> checkIfChainInRequestValid(@Valid @RequestBody List<Block> chain) {
        return ResponseEntity.ok(blockchainService.checkIfChainValid(chain));
    }
}
