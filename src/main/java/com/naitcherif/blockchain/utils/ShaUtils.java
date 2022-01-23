package com.naitcherif.blockchain.utils;

import com.naitcherif.blockchain.entities.Block;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public final class ShaUtils {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String ALGORITHM = "SHA-256";

    private ShaUtils() {
        throw new IllegalStateException("Utility class");
    }

    static byte[] digestBytes(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return md.digest(input);
    }

    static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String digest(String input) {
        return bytesToHex(digestBytes(input.getBytes(UTF_8)));
    }

    public static String digest(Instant timestamp, String lastHash, String data, int difficulty, int nones) {
        if (timestamp == null || data == null) {
            throw new IllegalArgumentException("timestamp or data is null");
        }
        return digest(timestamp + "-" + (lastHash == null ? "" : lastHash) + "-" + data + "-" + difficulty + "-" + nones);
    }

    public static String digest(Block previousBlock) {
        return digest(previousBlock.getTimestamp(),
                previousBlock.getLastHash(),
                previousBlock.getData(),
                previousBlock.getDifficulty(),
                previousBlock.getNones()
        );
    }
}