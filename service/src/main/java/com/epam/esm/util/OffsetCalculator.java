package com.epam.esm.util;

import org.springframework.stereotype.Component;

@Component
public class OffsetCalculator {

    public int calculate(int pageNumber, int pageSize){
        int offset = (pageNumber - 1) * pageSize;
        return offset;
    }
}
