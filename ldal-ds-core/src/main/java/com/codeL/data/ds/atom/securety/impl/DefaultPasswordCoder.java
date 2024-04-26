package com.codeL.data.ds.atom.securety.impl;

import com.codeL.data.ds.atom.securety.IPasswordCoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DefaultPasswordCoder implements IPasswordCoder {

    @Override
    public String encode(String encKey, String secret) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return secret;
    }

    @Override
    public String encode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return secret;
    }

    @Override
    public String decode(String encKey, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return secret;
    }

    @Override
    public char[] decode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return secret.toCharArray();
    }

}
