package com.epam.esm.validator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.CertificateParametersException;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.validator.impl.CertificateValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateValidatorImplTest {
    private CertificateValidator certificateValidator;
    private Certificate certificate;
    @Mock
    private CertificateService certificateService;

    @Before
    public void setUp(){
        certificateValidator = new CertificateValidatorImpl(certificateService);
        certificate = new Certificate();
        certificate.setName("certificate");
        certificate.setDescription("it's good certificate");
        certificate.setPrice(new BigDecimal("213.343"));
        certificate.setDuration(CertificateDuration.SIX_MONTH);
    }


    @Test(expected = ParameterException.class)
    public void validateExistenceCertificateByNameWhenCertificateWithSuchNameExistShouldThrowParameterException() {
        String certificateName = certificate.getName();
        boolean exceptionIfNotFound = false;
        when(certificateService.getByName(certificateName, exceptionIfNotFound)).thenReturn(certificate);

        certificateValidator.validateExistenceCertificateByName(certificateName);
        verify(certificateService, times(1)).getByName(certificateName, exceptionIfNotFound);
    }

    @Test
    public void validateExistenceCertificateByNameWhenCertificateWithSuchNameNoExistShouldEndWithoutError() {
        String certificateName = certificate.getName();
        boolean exceptionIfNotFound = false;
        when(certificateService.getByName(certificateName, exceptionIfNotFound)).thenReturn(null);

        certificateValidator.validateExistenceCertificateByName(certificateName);
        verify(certificateService, times(1)).getByName(certificateName, exceptionIfNotFound);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenNoValidPriceShouldThrowPriceLowerZeroException() {
        BigDecimal noValidPrice = new BigDecimal("-213.232");

        certificateValidator.validatePrice(noValidPrice);
    }

    @Test(expected = PriceException.class)
    public void validatePriceWhenPriceMoreFiveThousandShouldThrowPriceLowerZeroException() {
        BigDecimal priceLessZero = new BigDecimal("5500");

        certificateValidator.validatePrice(priceLessZero);
    }

    @Test
    public void validatePriceWhenPriceCorrectShouldEndWithoutErrors() {
        BigDecimal validPrice = new BigDecimal("213.232");

        certificateValidator.validatePrice(validPrice);
    }

    @Test
    public void validateDurationWhenDurationCorrectShouldEndWithoutErrors() {
        CertificateDuration duration = CertificateDuration.THREE_MONTH;

        certificateValidator.validateDuration(duration);
    }

    @Test(expected = CertificateParametersException.class)
    public void validateDurationWhenDurationNullShouldThrowCertificateParametersException() {
        CertificateDuration duration = null;

        certificateValidator.validateDuration(duration);
    }
}