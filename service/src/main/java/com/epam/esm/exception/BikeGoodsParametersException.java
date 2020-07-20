package com.epam.esm.exception;

public class BikeGoodsParametersException extends RuntimeException{
    public BikeGoodsParametersException() {}

    public BikeGoodsParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public BikeGoodsParametersException(String message) {
        super(message);
    }

    public BikeGoodsParametersException(Throwable e) {
        super(e);
    }
}
