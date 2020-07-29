package com.epam.esm.validator.impl;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.exception.CertificateParametersException;
import com.epam.esm.exception.ParameterException;
import com.epam.esm.exception.PriceException;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CertificateValidatorImpl extends AbstractValidator<Certificate> implements CertificateValidator {
    private final CertificateService certificateService;

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String MAX_PRICE = "5000";

    @Autowired
    public CertificateValidatorImpl(@Lazy CertificateService certificateService) {
        super(certificateService);
        this.certificateService=certificateService;
    }

    @Override
    public void validate(Certificate certificate) {
        BigDecimal price = certificate.getPrice();
        String name = certificate.getName();
        String description = certificate.getDescription();
        CertificateDuration duration = certificate.getDuration();

        validatePrice(price);
        validateString(name, NAME, false);
        validateExistenceCertificateByName(name);
        validateString(description, DESCRIPTION, false);
        validateDuration(duration);
    }

    @Override
    public void validateExistenceCertificateByName(String name) {
        Certificate certificate = certificateService.getByName(name.trim(), false);

        if (certificate != null) {
            throw new ParameterException("Certificate with: " + name + " name already exists");
        }
    }

    @Override
    public void validateForDuplicate(List<BikeGoods> goodsList, BikeGoods goods, long certificateId){
        if(goodsList.contains(goods)){
            throw new ParameterException("Such a connection certificateId:" +certificateId +
                    " - goodsId:" + goods.getId() +" already exists");
        }
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
