package com.epam.esm.validator;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.RegistrationException;
import com.epam.esm.service.api.UserService;
import com.epam.esm.validator.impl.UserValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorImplTest {
    private UserValidator userValidator;
    private User user;
    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        userValidator = new UserValidatorImpl(userService);
        user = new User();
        user.setUsername("Viktor");
        user.setPassword("Viktor1");
    }

    @Test(expected = ParameterException.class)
    public void validateExistenceUserByUsernameWhenUserWithSuchNameExistShouldThrowParameterException() {
        String username = user.getUsername();
        boolean exceptionIfNotFound = false;
        when(userService.getByUsername(username, exceptionIfNotFound)).thenReturn(user);

        userValidator.validateExistenceUserByUsername(username);
        verify(userService, times(1)).getByUsername(username, exceptionIfNotFound);
    }

    @Test
    public void validateExistenceUserByUsernameWhenUserWithSuchNameNoExistShouldEndWithoutError() {
        String username = user.getUsername();
        boolean exceptionIfNotFound = false;
        when(userService.getByUsername(username, exceptionIfNotFound)).thenReturn(null);

        userValidator.validateExistenceUserByUsername(username);
        verify(userService, times(1)).getByUsername(username, exceptionIfNotFound);
    }

    @Test(expected = RegistrationException.class)
    public void validateUsernameWhenUsernameLessThreeSymbolsShouldThrowRegistrationException(){
        String username = "Am";

        userValidator.validateUsername(username);
    }

    @Test(expected = RegistrationException.class)
    public void validateUsernameWhenUsernameMoreEighteenSymbolsShouldThrowRegistrationException(){
        String username = "ItsVeryBigNameForRegistrationOnOurSiteShouldBeLess";

        userValidator.validateUsername(username);
    }

    @Test(expected = RegistrationException.class)
    public void validateUsernameWhenUsernameContainsSpaceShouldThrowRegistrationException(){
        String username = "Slava Mistakius";

        userValidator.validateUsername(username);
    }

    @Test(expected = RegistrationException.class)
    public void validateUsernameWhenUsernameContainsAnyCharactersOtherThanLettersOrNumbersShouldThrowRegistrationException(){
        String username = "TheBestUser@i'm.by";

        userValidator.validateUsername(username);
    }

    @Test(expected = RegistrationException.class)
    public void validateUsernameWhenUsernameContainsOnlyNumbersShouldThrowRegistrationException(){
        String username = "123454";

        userValidator.validateUsername(username);
    }

    @Test
    public void validateUsernameWhenUsernameCorrectShouldEndWithoutError(){
        String username = "Andrey12";

        userValidator.validateUsername(username);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordNoContainsCapitalLetterShouldThrowRegistrationException(){
        String password = "andrey12";

        userValidator.validatePassword(password);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordNoContainsNumbersShouldThrowRegistrationException(){
        String password = "AndreyPassword";

        userValidator.validatePassword(password);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordLessSixSymbolsShouldThrowRegistrationException(){
        String password = "Mike1";

        userValidator.validatePassword(password);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordMoreTwentyFiveSymbolsShouldThrowRegistrationException(){
        String password = "ItsWillBeVeryBigPasswordForOursApplication123";

        userValidator.validatePassword(password);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordContainsOnlyNumbersShouldThrowRegistrationException(){
        String password = "1234567890";

        userValidator.validatePassword(password);
    }

    @Test(expected = RegistrationException.class)
    public void validatePasswordWhenPasswordContainsSpaceThrowRegistrationException(){
        String password = "Andrey12 password";

        userValidator.validatePassword(password);
    }

    @Test
    public void validatePasswordWhenPasswordContainsAnyCharactersOtherThanLettersOrNumbersBotNoSpaceShouldEndWithoutError(){
        String password = "Andrey12.cool@boy";

        userValidator.validatePassword(password);
    }

    @Test
    public void validatePasswordWhenPasswordCorrectShouldEndWithoutError(){
        String password = "Andrey12";

        userValidator.validatePassword(password);
    }
}
