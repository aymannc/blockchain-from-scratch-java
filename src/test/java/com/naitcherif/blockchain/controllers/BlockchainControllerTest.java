package com.naitcherif.blockchain.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naitcherif.blockchain.dtos.request.MineBlockRequestDTO;
import com.naitcherif.blockchain.entities.Block;
import com.naitcherif.blockchain.entities.Blockchain;
import com.naitcherif.blockchain.services.IBlockchainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BlockchainController.class)
class BlockchainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IBlockchainService blockchainService;

    @Test
    void shouldReturnChainContainingGenesisBlock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/blocks")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(blockchainService, times(1)).getChain();
    }

    @Test
    void mineBlock__ShouldReturn415Status() throws Exception {
        var dto = new MineBlockRequestDTO("Hello Block");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/blocks")
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void mineBlock__ShouldReturn200Status() throws Exception {
        var dto = new MineBlockRequestDTO("Hello Block");
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/blocks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
        ArgumentCaptor<MineBlockRequestDTO> argumentCaptor = ArgumentCaptor.forClass(MineBlockRequestDTO.class);
        verify(blockchainService, times(1)).mineBlock(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().data(), dto.data());
    }

    @Test
    void checkIfLocalChainValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/blocks/validate")
        ).andExpect(status().isOk());
        verify(blockchainService, times(1)).checkIfChainValid();
    }

    @Test
    void checkIfChainInRequestValid() throws Exception {
        List<Block> chain = new Blockchain().getChain();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/blocks/validate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(chain))
        ).andExpect(status().isOk());
        final ArgumentCaptor<List<Block>> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(blockchainService, times(1)).checkIfChainValid(listCaptor.capture());
        assertAll(
                () -> assertEquals(listCaptor.getValue().size(), listCaptor.getValue().size()),
                () -> assertEquals(listCaptor.getValue().get(0), listCaptor.getValue().get(0))
        );
    }
}