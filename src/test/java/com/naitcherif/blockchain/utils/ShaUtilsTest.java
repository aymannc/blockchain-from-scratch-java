package com.naitcherif.blockchain.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

@DisplayName("SHA-256 utility functions tests")
class ShaUtilsTest {
    final String data = "Hello Blockchain";
    final String expectedHash = "7cf88f2ee398c0b7c0e760a1dccaf3571e0baccf310f11fe3bdfd0b09675ea75";

    @Test
    @DisplayName("Test SHA-256 method")
    void digest() {
        String digest = ShaUtils.digest(data);
        System.out.println(digest);
        Assertions.assertAll(
                () -> Assertions.assertFalse(digest.isBlank()),
                () -> Assertions.assertEquals(digest, expectedHash)
        );
    }

    @Test
    @DisplayName("Test string data to digested bytes conversion method")
    void digestBytes() {
        var digestBytes = ShaUtils.digestBytes(data.getBytes(StandardCharsets.UTF_8));
        Assertions.assertTrue(digestBytes.length > 0);
    }

    @Test
    @DisplayName("Test digested bytes to string conversion method")
    void bytesToHex() {
        var digestBytes = ShaUtils.bytesToHex(data.getBytes(StandardCharsets.UTF_8));
        Assertions.assertFalse(digestBytes.isEmpty());
    }
}