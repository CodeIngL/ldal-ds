package com.codeL.data.ds.atom.securety.impl;

import com.codeL.data.ds.atom.securety.IPasswordCoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PasswordCoder implements IPasswordCoder {

    public IPasswordCoder delegate = null;

    public PasswordCoder() {
        delegate = new DefaultPasswordCoder();

    }

    @Override
    public String encode(String encKey, String secret) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        return delegate.encode(encKey, secret);
    }

    @Override
    public String encode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        return delegate.encode(secret);
    }

    @Override
    public String decode(String encKey, String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        return delegate.decode(encKey, secret);
    }

    @Override
    public char[] decode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        return delegate.decode(secret);
    }

}
