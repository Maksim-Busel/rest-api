package com.epam.esm.validator.impl;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.CertificateParametersException;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.Service;
import com.epam.esm.validator.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CertificateValidatorImpl extends AbstractValidatorImpl<Certificate> implements CertificateValidator {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String MAX_PRICE = "5000";

    @Autowired
    public CertificateValidatorImpl(@Lazy CertificateService certificateService) {
        super(certificateService);
    }

    @Override
    public void validate(Certificate certificate) {
        BigDecimal price = certificate.getPrice();
        String name = certificate.getName();
        String description = certificate.getDescription();
        CertificateDuration duration = certificate.getDuration();

        validatePrice(price);
        validateString(name, NAME, false);
        validateString(description, DESCRIPTION, false);
        validateDuration(duration);
    }

    @Override
    public void validatePrice(BigDecimal price) {
        BigDecimal maxPrice = new BigDecimal(MAX_PRICE);
        BigDecimal minPrice = BigDecimal.ZERO;

        if (price == null || price.compareTo(minPrice) < 0 || price.compareTo(maxPrice) > 0) {
            throw new PriceException("The price you entered is incorrect.");
        }
    }

    @Override
    public void validateDuration(CertificateDuration duration) {
        if (duration == null) {
            throw new CertificateParametersException("You haven't entered the certificate duration parameter");
        }
    }
}
