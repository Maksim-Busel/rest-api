package com.epam.esm.service.impl;

import com.epam.esm.dao.api.CertificateDao;
import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.CertificateParametersException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.ThereIsNoSuchCertificateException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.util.OffsetCalculator;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.Validator;
import org.hibernate.exception.ConstraintViolationException;
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
public class CertificateServiceImpl extends AbstractService<Certificate> implements CertificateService {
    private final Validator<BikeGoods> bikeGoodsValidator;
    private final CertificateDao certificateDao;
    private final CertificateValidator certificateValidator;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, CertificateValidator certificateValidator,
                                  Validator<BikeGoods> bikeGoodsValidator, OffsetCalculator offsetCalculator) {
        super(certificateValidator, certificateDao, offsetCalculator);
        this.bikeGoodsValidator = bikeGoodsValidator;
        this.certificateValidator = certificateValidator;
        this.certificateDao = certificateDao;
    }

    @Override
    @Transactional
    public Certificate add(Certificate certificate) {
        certificateValidator.validate(certificate);
        certificate.setDateCreation(LocalDate.now());

        return dao.create(certificate);
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
        validator.validate(updatedCertificate);

        Certificate originCertificate = getById(updatedCertificate.getId());
        updatedCertificate.setDateModification(LocalDate.now());
        updatedCertificate.setDateCreation(originCertificate.getDateCreation());

        return certificateDao.update(updatedCertificate);
    }

    @Override
    @Transactional
    public void useCertificateBuyBikeGoods(long certificateId, long[] bikeGoodsId) {
        if (bikeGoodsId.length == 0) {
            throw new IncorrectDataException("You don't specify any bike goods id");
        }
        validator.validateExistenceEntityById(certificateId);

        for (long goodsId : bikeGoodsId) {
            bikeGoodsValidator.validateExistenceEntityById(goodsId);
            int result = certificateDao.createCertificateBikeGoods(certificateId, goodsId);

            if (result == 0) {
                throw new CertificateParametersException("Failed to use certificate to purchase goods");
            }
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
    public List<Certificate> findByTagsId(List<Integer> tagsId, int pageNumber, int pageSize) {
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

        Certificate certificateForEdit = dao.findById(certificateId);

        changePartCertificate(certificateForEdit, updatedCertificate);
        certificateForEdit.setDateModification(LocalDate.now());

        return certificateDao.update(certificateForEdit);
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
