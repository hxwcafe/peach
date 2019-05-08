package com.example.peach.service;

import java.security.InvalidAlgorithmParameterException;

public interface AesCbc{
    byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException;
}
