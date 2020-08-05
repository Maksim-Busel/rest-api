package com.epam.esm.util;

import org.junit.Assert;
import org.junit.Test;

public class OffsetCalculatorTest {
    private final OffsetCalculator offsetCalculator = new OffsetCalculator();

    @Test
    public void calculateWhenPageNumberTenAndPageSizeTenShouldReturnNinety(){
        int pageNumber = 10;
        int pageSize = 10;
        int offsetExpected = 90;

        int result = offsetCalculator.calculate(pageNumber, pageSize);

        Assert.assertEquals(offsetExpected, result);
    }

    @Test
    public void calculateWhenPageNumberOneAndPageSizeTenShouldReturnZero(){
        int pageNumber = 1;
        int pageSize = 10;
        int offsetExpected = 0;

        int result = offsetCalculator.calculate(pageNumber, pageSize);

        Assert.assertEquals(offsetExpected, result);
    }

    @Test
    public void calculateWhenPageNumberTwoAndPageSizeHundredShouldReturnHundred(){
        int pageNumber = 2;
        int pageSize = 100;
        int offsetExpected = 100;

        int result = offsetCalculator.calculate(pageNumber, pageSize);

        Assert.assertEquals(offsetExpected, result);
    }
}
