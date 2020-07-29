package com.epam.esm.service.impl;

import com.epam.esm.dao.api.CertificateDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.*;
import com.epam.esm.service.api.BikeGoodsService;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Service
public class CertificateServiceImpl extends AbstractService<Certificate> implements CertificateService {
    private final Validator<BikeGoods> bikeGoodsValidator;
    private final CertificateDao certificateDao;
    private final CertificateValidator certificateValidator;
    private final BikeGoodsService bikeGoodsService;

    private static final String CERTIFICATE_NAME_FIELD = "certificate name";
    private static final String DESCRIPTION_FIELD = " description";

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, CertificateValidator certificateValidator,
                                  Validator<BikeGoods> bikeGoodsValidator, OffsetCalculator offsetCalculator,
                                  BikeGoodsService bikeGoodsService) {
        super(certificateValidator, certificateDao, offsetCalculator);
        this.bikeGoodsValidator = bikeGoodsValidator;
        this.certificateValidator = certificateValidator;
        this.certificateDao = certificateDao;
        this.bikeGoodsService = bikeGoodsService;
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        certificateValidator.validate(certificate);
        certificate.setDateCreation(LocalDate.now());

        return dao.create(certificate);
    }

    @Override
    public Certificate getByName(String certificateName, boolean exceptionIfNotFound) {
        validator.validateString(certificateName, CERTIFICATE_NAME_FIELD);

        try {
            return certificateDao.findByName(certificateName);
        } catch (EmptyResultDataAccessException e) {
            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchEntityException("Certificate: " + certificateName + " doesn't exist", e);
            }

            return null;
        }
    }

    @Override
    public Certificate getByName(String certificateName) {
        return this.getByName(certificateName, true);
    }

    @Override
    public List<Certificate> getFilteredList(String tagFieldValue, String searchBy, String sortBy,
                                             int pageNumber, int pageSize) {

        validator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        try {
            return certificateDao.findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);

        } catch (InvalidDataAccessResourceUsageException e) {
            throw new ParameterException("Incorrect filtering parameters. Change the parameters and try again.");
        } catch (EmptyResultDataAccessException e) {
            throw new ThereIsNoSuchCertificateException("Certificate you specified does not exist", e);
        }
    }

    @Override
    @Transactional
    public Certificate edit(Certificate updatedCertificate) {
        long certificateId = updatedCertificate.getId();
        validator.validateExistenceEntityById(certificateId);
        Certificate certificateFromDb = certificateDao.findById(certificateId);

        String updatedCertificateName = updatedCertificate.getName().trim();
        String updatedDescription = updatedCertificate.getDescription();
        BigDecimal updatedPrice = updatedCertificate.getPrice();
        CertificateDuration updatedDuration = updatedCertificate.getDuration();

        if (certificateFromDb.getName().trim().equals(updatedCertificateName)) {
            certificateValidator.validateString(updatedDescription, DESCRIPTION_FIELD);
            certificateValidator.validatePrice(updatedPrice);
            certificateValidator.validateDuration(updatedDuration);
        } else {
            validator.validate(updatedCertificate);
            certificateFromDb.setName(updatedCertificateName);
        }

        certificateFromDb.setDescription(updatedDescription);
        certificateFromDb.setPrice(updatedPrice);
        certificateFromDb.setDuration(updatedDuration);
        certificateFromDb.setDateModification(LocalDate.now());

        return certificateFromDb;
    }

    @Override
    @Transactional
    public void useCertificateBuyBikeGoods(long certificateId, long[] bikeGoodsId) {
        if (bikeGoodsId.length == 0) {
            throw new IncorrectDataException("You don't specify any bike goods id");
        }
        validator.validateExistenceEntityById(certificateId);

        Certificate certificateFromDb = dao.findById(certificateId);
        List<BikeGoods> goodsList = certificateFromDb.getGoods();

        for (long concreteGoodsId : bikeGoodsId) {
            BikeGoods goodsFromDb = bikeGoodsService.getById(concreteGoodsId);

            certificateValidator.validateForDuplicate(goodsList, goodsFromDb, certificateId);
            goodsFromDb.addCertificate(certificateFromDb);
        }
    }

    @Override
    public BigDecimal getCostCertificates(List<Integer> certificatesId) {
        for (long id : certificatesId) {
            validator.validateExistenceEntityById(id);
        }

        return certificateDao.findCostCertificates(certificatesId);
    }

    @Override
    public List<Certificate> getByTagsId(List<Integer> tagsId, int pageNumber, int pageSize) {
        int tagsCount = tagsId.size();
        if (tagsCount == 0) {
            throw new IncorrectDataException("You don't specify any tag id");
        }

        for (int id : tagsId) {
            bikeGoodsValidator.validateExistenceEntityById(id);
        }

        int offset = offsetCalculator.calculate(pageNumber, pageSize);
        return certificateDao.findByTagsId(tagsId, tagsCount, offset, pageSize);
    }

    @Override
    @Transactional
    public Certificate editPart(Certificate updatedCertificate) {
        long certificateId = updatedCertificate.getId();
        validator.validateExistenceEntityById(certificateId);
        Certificate certificateFromDb = certificateDao.findById(certificateId);

        changePartCertificate(certificateFromDb, updatedCertificate);
        certificateFromDb.setDateModification(LocalDate.now());

        return certificateFromDb;
    }

    private void changePartCertificate(Certificate certificateFromDatabase, Certificate updatedCertificate) {

        String updatedName = updatedCertificate.getName();
        if (isNoneBlank(updatedName)) {
            certificateValidator.validateExistenceCertificateByName(updatedName);
            certificateFromDatabase.setName(updatedName);
        }

        String updatedDescription = updatedCertificate.getDescription();
        if (isNoneBlank(updatedDescription)) {
            certificateValidator.validateString(updatedDescription, DESCRIPTION_FIELD);
            certificateFromDatabase.setDescription(updatedDescription);
        }

        BigDecimal updatedPrice = updatedCertificate.getPrice();
        if (updatedPrice != null) {
            certificateValidator.validatePrice(updatedPrice);
            certificateFromDatabase.setPrice(updatedPrice);
        }

        CertificateDuration updatedDuration = updatedCertificate.getDuration();
        if (updatedDuration != null) {
            certificateFromDatabase.setDuration(updatedDuration);
        }
    }
}
