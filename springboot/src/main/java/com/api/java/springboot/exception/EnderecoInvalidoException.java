package com.api.java.springboot.exception;

public class EnderecoInvalidoException extends RuntimeException {
    public EnderecoInvalidoException(String message) {
        super(message);
    }
}
