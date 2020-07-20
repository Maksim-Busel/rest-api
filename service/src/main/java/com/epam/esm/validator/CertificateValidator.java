package com.epam.esm.validator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;

import java.math.BigDecimal;

public interface CertificateValidator extends Validator<Certificate> {

    void validatePrice(BigDecimal price);

    void validateDuration(CertificateDuration duration);

}
