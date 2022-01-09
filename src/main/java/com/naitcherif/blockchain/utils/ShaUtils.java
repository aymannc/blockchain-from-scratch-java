package com.naitcherif.blockchain.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}