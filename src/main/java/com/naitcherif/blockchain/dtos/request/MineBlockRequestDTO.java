package com.naitcherif.blockchain.dtos.request;

import javax.validation.constraints.NotEmpty;

public record MineBlockRequestDTO(@NotEmpty String data) {
}
