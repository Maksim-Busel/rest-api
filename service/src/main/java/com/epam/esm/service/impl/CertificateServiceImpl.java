package com.epam.esm.service.impl;

import com.epam.esm.dao.api.CertificateDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.*;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao dao;
    private final CertificateValidator certificateValidator;
    private final Validator<BikeGoods> bikeGoodsValidator;
    private final OffsetCalculator offsetCalculator;

    @Autowired
    public CertificateServiceImpl(CertificateDao dao, CertificateValidator certificateValidator,
                                  Validator<BikeGoods> bikeGoodsValidator, OffsetCalculator offsetCalculator) {
        this.dao = dao;
        this.certificateValidator = certificateValidator;
        this.bikeGoodsValidator = bikeGoodsValidator;
        this.offsetCalculator = offsetCalculator;
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        certificateValidator.validate(certificate);
        certificate.setDateCreation(LocalDate.now());

        try {
            return dao.create(certificate);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException("Certificate with: " + certificate.getName() + " name already exists.");
        }
    }

    @Override
    public Certificate getById(long id, boolean exceptionIfNotFound) {
        certificateValidator.validateIdValue(id);

        try {
            return dao.findById(id);
        } catch (EmptyResultDataAccessException e) {

            if (exceptionIfNotFound) {
                throw new ThereIsNoSuchCertificateException("Certificate: " + id + " doesn't exist", e);
            }
            return null;
        }
    }

    @Override
    public Certificate getById(long id) {
        return this.getById(id, true);
    }

    @Override
    public List<Certificate> getFilteredList(String tagFieldValue, String searchBy, String sortBy,
                                             int pageNumber, int pageSize) {

        certificateValidator.validatePageParameters(pageNumber, pageSize);
        int offset = offsetCalculator.calculate(pageNumber, pageSize);

        try {
            List<Certificate> certificates = dao.findFiltered(tagFieldValue, searchBy, sortBy, offset, pageSize);

            if (certificates.size() == 0) {
                throw new ThereIsNoSuchCertificateException("Not found any certificates for your request");
            }

            return certificates;
        } catch (InvalidDataAccessResourceUsageException e) {
            throw new ParameterException("Incorrect filtering parameters. Change the parameters and try again.");
        } catch (EmptyResultDataAccessException e) {
            throw new ThereIsNoSuchBikeGoodsException("Goods you specified does not exist", e);
        }
    }

    @Override
    @Transactional
    public void lock(long id) {
        certificateValidator.validateExcitingEntityById(id);
        int result = dao.lockById(id);

        if (result == 0) {
            throw new FailedOperationException("Failed to delete certificate");
        }
    }

    @Override
    @Transactional
    public Certificate edit(Certificate updatedCertificate) {
        certificateValidator.validate(updatedCertificate);
        Certificate originCertificate = getById(updatedCertificate.getId());

        updatedCertificate.setDateModification(LocalDate.now());
        updatedCertificate.setDateCreation(originCertificate.getDateCreation());

        try {
            return dao.update(updatedCertificate);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException("Certificate with: " + updatedCertificate.getName() + " name already exists.");
        }
    }

    @Override
    @Transactional
    public void useCertificateBuyBikeGoods(long certificateId, long[] bikeGoodsId) {
        if (bikeGoodsId.length == 0) {
            throw new IncorrectDataException("You don't specify any bike goods id");
        }
        certificateValidator.validateExcitingEntityById(certificateId);

        for (long goodsId : bikeGoodsId) {
            bikeGoodsValidator.validateExcitingEntityById(goodsId);
            int result = dao.createCertificateBikeGoods(certificateId, goodsId);

            if (result == 0) {
                throw new CertificateParametersException("Failed to use certificate to purchase goods");
            }
        }
    }

    @Override
    public BigDecimal getCostCertificates(List<Integer> certificatesId) {
        for (long id : certificatesId) {
            certificateValidator.validateExcitingEntityById(id);
        }

        return dao.findCostCertificates(certificatesId);
    }

    @Override
    public List<Certificate> findByTagsId(List<Integer> tagsId, int pageNumber, int pageSize) {
        int tagsCount = tagsId.size();
        if (tagsCount == 0) {
            throw new IncorrectDataException("You don't specify any tag id");
        }

        for (int id : tagsId) {
            bikeGoodsValidator.validateExcitingEntityById(id);
        }

        int offset = offsetCalculator.calculate(pageNumber, pageSize);
        List<Certificate> certificates = dao.findByTagsId(tagsId, tagsCount, offset, pageSize);
        if (certificates.size() == 0) {
            throw new ThereIsNoSuchCertificateException("There are no certificates with this list tags");
        }

        return certificates;
    }

    @Override
    @Transactional
    public Certificate editPart(Certificate updatedCertificate) {
        long certificateId = updatedCertificate.getId();
        certificateValidator.validateExcitingEntityById(certificateId);

        Certificate certificateForEdit = dao.findById(certificateId);

        changePartCertificate(certificateForEdit, updatedCertificate);
        certificateForEdit.setDateModification(LocalDate.now());

        try {
            return dao.create(certificateForEdit);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectDataException("Certificate with: " + updatedCertificate.getName() + " name already exists.");
        }
    }

    private void changePartCertificate(Certificate certificateFromDatabase, Certificate changedCertificate) {

        String name = changedCertificate.getName();
        if (isNoneBlank(name)) {
            certificateFromDatabase.setName(name);
        }

        String description = changedCertificate.getDescription();
        if (isNoneBlank(description)) {
            certificateFromDatabase.setDescription(description);
        }

        BigDecimal price = changedCertificate.getPrice();
        if (price != null) {
            certificateValidator.validatePrice(price);
            certificateFromDatabase.setPrice(price);
        }

        CertificateDuration duration = changedCertificate.getDuration();
        if (duration != null) {
            certificateFromDatabase.setDuration(duration);
        }
    }
}
