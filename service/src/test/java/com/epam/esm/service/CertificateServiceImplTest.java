package com.epam.esm.service;

import com.epam.esm.dao.api.CertificateDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.*;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.Validator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {
    private static final String CERTIFICATE_NAME_FIELD = "certificate name";
    private static final String DESCRIPTION_FIELD = " description";
    private final int pageNumber = 1;
    private final int pageSize = 10;

    @Mock
    private CertificateDao certificateDao;
    @Mock
    private CertificateValidator certificateValidator;
    @Mock
    private Validator<BikeGoods> bikeGoodsValidator;
    @Mock
    private BikeGoodsService bikeGoodsService;
    @Mock
    private OffsetCalculator calculator;

    private CertificateServiceImpl certificateService;

    private Certificate certificate;

    @Before
    public void setUp() {
        certificateService = new CertificateServiceImpl(certificateDao, certificateValidator, bikeGoodsValidator,
                calculator, bikeGoodsService);
        certificate = new Certificate();
        certificate.setId(1);
        certificate.setName("certificate");
        certificate.setDescription("it's good certificate");
        certificate.setPrice(new BigDecimal("213.343"));
        certificate.setDuration(CertificateDuration.SIX_MONTH);
    }

    @Test
    public void addWhenCertificateCorrectShouldExecuteCreateAndValidateOneTime() {
        certificateService.add(certificate);

        verify(certificateValidator, times(1)).validate(certificate);
        verify(certificateDao, times(1)).create(certificate);
    }

    @Test
    public void addWhenCertificateWithoutDateCreationShouldSetDateCreationNow() {
        Certificate certificateForTest = new Certificate();

        certificateService.add(certificateForTest);

        Assert.assertEquals(LocalDate.now(), certificateForTest.getСreationDate());
    }

    @Test
    public void getByNameWhenExistEntityWithThisNameShouldExecuteValidateStringAndFindByNameOneTime() {
        String certificateName = "name";
        boolean exceptionIfNotFound = true;

        certificateService.getByName(certificateName, exceptionIfNotFound);

        verify(certificateValidator, times(1)).validateString(certificateName, CERTIFICATE_NAME_FIELD);
        verify(certificateDao, times(1)).findByName(certificateName);
    }

    @Test(expected = ThereIsNoSuchEntityException.class)
    public void getByNameWhenNoExistEntityWithThisNameAndExceptionIfNotFoundTrueShouldThrowThereIsNoSuchEntityException() {
        String certificateName = "name";
        boolean exceptionIfNotFound = true;
        doThrow(EmptyResultDataAccessException.class).when(certificateDao).findByName(certificateName);

        certificateService.getByName(certificateName, exceptionIfNotFound);

        verify(certificateValidator, times(1)).validateString(certificateName, CERTIFICATE_NAME_FIELD);
        verify(certificateDao, times(1)).findByName(certificateName);
    }

    @Test
    public void getByNameWhenNoExistEntityWithThisNameAndExceptionIfNotFoundFalseShouldReturnNull() {
        String certificateName = "name";
        boolean exceptionIfNotFound = false;
        doThrow(EmptyResultDataAccessException.class).when(certificateDao).findByName(certificateName);

        Certificate nullCertificate = certificateService.getByName(certificateName, exceptionIfNotFound);

        Assert.assertNull(nullCertificate);
        verify(certificateValidator, times(1)).validateString(certificateName, CERTIFICATE_NAME_FIELD);
        verify(certificateDao, times(1)).findByName(certificateName);
    }

    @Test
    public void getFilteredListWhenAnyParametersCorrectShouldExecuteThreeMethodsOneTime() {
        int offset = 0;
        String tagFieldValue = "tagField";
        String searchBy = "searchBy";
        String sortBy = "sortBy";
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);

        certificateService.getFilteredList(tagFieldValue, searchBy, sortBy, pageNumber, pageSize);

        verify(certificateValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(certificateDao, times(1)).findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);
    }

    @Test(expected = ParameterException.class)
    public void getFilteredListWhenDaoThrowInvalidDataAccessResourceUsageExceptionShouldCatchAndThrowParameterException() {
        int offset = 0;
        String tagFieldValue = "tagField";
        String searchBy = "searchBy";
        String sortBy = "sortBy";
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        doThrow(InvalidDataAccessResourceUsageException.class).when(certificateDao).findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);

        certificateService.getFilteredList(tagFieldValue, searchBy, sortBy, pageNumber, pageSize);

        verify(certificateValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(certificateDao, times(1)).findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);
    }

    @Test(expected = ThereIsNoSuchCertificateException.class)
    public void getFilteredListWhenDaoThrowEmptyResultDataAccessExceptionShouldCatchAndThrowThereIsNoSuchCertificateException() {
        int offset = 0;
        String tagFieldValue = "tagField";
        String searchBy = "searchBy";
        String sortBy = "sortBy";
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(0);
        doThrow(EmptyResultDataAccessException.class).when(certificateDao).findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);

        certificateService.getFilteredList(tagFieldValue, searchBy, sortBy, pageNumber, pageSize);

        verify(certificateValidator, times(1)).validatePageParameters(pageNumber, pageSize);
        verify(calculator, times(1)).calculate(pageNumber, pageSize);
        verify(certificateDao, times(1)).findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);
    }

    @Test
    public void editWhenUpdatedCertificateNameEqualsCertificateNameFromDbShouldExecuteValidateStringValidatePriceValidateDurationOneTime() {
        Certificate certificateFromDb = new Certificate();
        certificateFromDb.setId(1);
        certificateFromDb.setName("certificate");
        certificateFromDb.setDescription("it's good certificate");
        certificateFromDb.setPrice(new BigDecimal("213.343"));
        certificateFromDb.setDuration(CertificateDuration.SIX_MONTH);
        long certificateId = certificate.getId();
        when(certificateDao.findById(1)).thenReturn(certificateFromDb);

        certificateService.edit(certificate);

        verify(certificateValidator, times(1)).validateExistenceEntityById(certificateId);
        verify(certificateDao, times(1)).findById(certificateId);
        verify(certificateValidator, times(1)).validateString(certificate.getDescription(), DESCRIPTION_FIELD);
        verify(certificateValidator, times(1)).validatePrice(certificate.getPrice());
        verify(certificateValidator, times(1)).validateDuration(certificate.getDuration());
    }

    @Test
    public void editWhenUpdatedCertificateNameNoEqualsCertificateNameFromDbShouldExecuteValidateOneTime() {
        long certificateId = 1;
        Certificate certificateFromDb = new Certificate();
        certificateFromDb.setId(certificateId);
        certificateFromDb.setName("another certificate");
        certificateFromDb.setDescription("it's good certificate");
        certificateFromDb.setPrice(new BigDecimal("213.343"));
        certificateFromDb.setDuration(CertificateDuration.SIX_MONTH);
        when(certificateDao.findById(certificateId)).thenReturn(certificateFromDb);

        certificateService.edit(certificate);

        verify(certificateValidator, times(1)).validateExistenceEntityById(certificateId);
        verify(certificateDao, times(1)).findById(certificateId);
        verify(certificateValidator, times(1)).validate(certificate);
    }

    @Test
    public void editWhenUpdatedCertificateDifferCertificateFromDbShouldChangeCertificateFromDbThatTheyWillBeEquals() {
        long certificateId = certificate.getId();
        Certificate certificateFromDb = new Certificate();
        certificateFromDb.setId(certificateId);
        certificateFromDb.setName("another certificate");
        certificateFromDb.setDescription("it's certificate");
        certificateFromDb.setPrice(new BigDecimal("223.343"));
        certificateFromDb.setDuration(CertificateDuration.ONE_MONTH);

        Certificate expectedCertificate = new Certificate();
        expectedCertificate.setId(certificateId);
        expectedCertificate.setName("certificate");
        expectedCertificate.setDescription("it's good certificate");
        expectedCertificate.setPrice(new BigDecimal("213.343"));
        expectedCertificate.setDuration(CertificateDuration.SIX_MONTH);
        when(certificateDao.findById(certificateId)).thenReturn(certificateFromDb);

        Certificate result = certificateService.edit(certificate);

        Assert.assertEquals(expectedCertificate, result);
        verify(certificateValidator, times(1)).validateExistenceEntityById(certificateId);
        verify(certificateDao, times(1)).findById(certificateId);
        verify(certificateValidator, times(1)).validate(certificate);
    }

    @Test
    public void getCostCertificatesWhenThreCertificatesIdShouldExecuteValidateExistenceEntityByIdThreeTimes() {
        int firstCertificateId = 1;
        int secondCertificateId = 2;
        int thirdCertificateId = 3;

        List<Integer> certificatesId = new ArrayList<>();
        certificatesId.add(firstCertificateId);
        certificatesId.add(secondCertificateId);
        certificatesId.add(thirdCertificateId);

        certificateService.getCostCertificates(certificatesId);

        verify(certificateValidator, times(1)).validateExistenceEntityById(firstCertificateId);
        verify(certificateValidator, times(1)).validateExistenceEntityById(secondCertificateId);
        verify(certificateValidator, times(1)).validateExistenceEntityById(thirdCertificateId);
        verify(certificateDao, times(1)).findCostCertificates(certificatesId);
    }

    @Test
    public void getByTagsIdWhenThreeTagsIdShouldExecutevalidateExistenceEntityByIdThreeTimes() {
        int firstTagId = 1;
        int secondTagId = 2;
        int thirdTagId = 3;

        List<Integer> tagsId = new ArrayList<>();
        tagsId.add(firstTagId);
        tagsId.add(secondTagId);
        tagsId.add(thirdTagId);

        int offset = 0;
        int tagsCount = 3;
        when(calculator.calculate(pageNumber, pageSize)).thenReturn(offset);

        certificateService.getByTagsId(tagsId, pageNumber, pageSize);

        verify(bikeGoodsValidator, times(1)).validateExistenceEntityById(firstTagId);
        verify(bikeGoodsValidator, times(1)).validateExistenceEntityById(secondTagId);
        verify(bikeGoodsValidator, times(1)).validateExistenceEntityById(thirdTagId);
        verify(certificateDao, times(1)).findByTagsId(tagsId, tagsCount, offset, pageSize);
    }

    @Test(expected = IncorrectDataException.class)
    public void getByTagsIdWhenZeroTagsIdShouldThrowIncorrectDataException() {
        List<Integer> tagsId = new ArrayList<>();

        certificateService.getByTagsId(tagsId, pageNumber, pageSize);
    }
}