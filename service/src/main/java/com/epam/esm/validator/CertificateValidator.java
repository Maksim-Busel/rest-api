package com.epam.esm.validator;

import com.epam.esm.entity.BikeGoods;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDuration;

import java.math.BigDecimal;
import java.util.List;

public interface CertificateValidator extends Validator<Certificate> {

    void validatePrice(BigDecimal price);

    void validateDuration(CertificateDuration duration);

    void validateExistenceCertificateByName(String name);

    void validateForDuplicate(List<BikeGoods> goodsList, BikeGoods goods, long certificateId);
}
