package com.epam.esm.validator;

import com.epam.esm.exception.ParameterException;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.validator.impl.AbstractValidator;
import com.epam.esm.validator.impl.BikeGoodsValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AbstractValidatorTest {
    private AbstractValidator abstractValidator;
    @Mock
    private BikeGoodsService bikeGoodsService;


    @Before
    public void setUp() {
        abstractValidator = new BikeGoodsValidatorImpl(bikeGoodsService);
    }

    @Test(expected = ParameterException.class)
    public void validateStringWhenStringNullAndEmptyValueAllowedFalseShouldThrowParameterException(){
        boolean emptyValueAllowed = false;
        String someString = null;
        String someStringField = "name";

        abstractValidator.validateString(someString, someStringField, emptyValueAllowed);
    }

    @Test
    public void validateStringWhenStringNullAndEmptyValueAllowedTrueShouldEndWithoutErrors(){
        boolean emptyValueAllowed = true;
        String someString = null;
        String someStringField = "name";

        abstractValidator.validateString(someString, someStringField, emptyValueAllowed);
    }

    @Test(expected = ParameterException.class)
    public void validateStringWhenStringBlankAndEmptyValueAllowedFalseShouldThrowParameterException(){
        boolean emptyValueAllowed = false;
        String someString = " ";
        String someStringField = "name";

        abstractValidator.validateString(someString, someStringField, emptyValueAllowed);
    }

    @Test
    public void validateStringWhenStringNonBlankAndEmptyValueAllowedFalseShouldEndWithoutErrors(){
        boolean emptyValueAllowed = true;
        String someString = "correct name";
        String someStringField = "name";

        abstractValidator.validateString(someString, someStringField, emptyValueAllowed);
    }

    @Test(expected = ParameterException.class)
    public void validatePageParameterWhenPageNumberLessOneShouldThrowParameterException(){
        int pageNumber = 0;
        int pageSize = 10;

        abstractValidator.validatePageParameters(pageNumber, pageSize);
    }

    @Test(expected = ParameterException.class)
    public void validatePageParameterWhenPageNumberMoreTenMillionsShouldThrowParameterException(){
        int pageNumber = 11_000_000;
        int pageSize = 10;

        abstractValidator.validatePageParameters(pageNumber, pageSize);
    }

    @Test(expected = ParameterException.class)
    public void validatePageParameterWhenPageSizeMoreHundredShouldThrowParameterException(){
        int pageNumber = 1;
        int pageSize = 200;

        abstractValidator.validatePageParameters(pageNumber, pageSize);
    }

    @Test(expected = ParameterException.class)
    public void validatePageParameterWhenPageSizeLessOneShouldThrowParameterException(){
        int pageNumber = 1;
        int pageSize = 0;

        abstractValidator.validatePageParameters(pageNumber, pageSize);
    }

    @Test
    public void validatePageParameterWhenPageSizeAndPageNumberCorrectShouldEndWithoutError(){
        int pageNumber = 1;
        int pageSize = 10;

        abstractValidator.validatePageParameters(pageNumber, pageSize);
    }


    @Test(expected = ParameterException.class)
    public void validateIdValueWhenIdLessOneShouldThrowParameterException() {
        long id = 0;

        abstractValidator.validateIdValue(id);
    }

    @Test
    public void validateIdValueWhenIdMoreThanOneShouldEndWithoutErrors() {
        long id = 123;

        abstractValidator.validateIdValue(id);
    }
}
