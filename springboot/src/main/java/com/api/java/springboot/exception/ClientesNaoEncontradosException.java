package com.api.java.springboot.exception;

public class ClientesNaoEncontradosException extends RuntimeException {
    public ClientesNaoEncontradosException(String message) {
        super(message);
    }
}