package com.epam.esm.exception;

public class ThereIsNoSuchBikeGoodsException extends RuntimeException{
    public ThereIsNoSuchBikeGoodsException() {}

    public ThereIsNoSuchBikeGoodsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThereIsNoSuchBikeGoodsException(String message) {
        super(message);
    }

    public ThereIsNoSuchBikeGoodsException(Throwable e) {
        super(e);
    }
}
