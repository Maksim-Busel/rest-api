package com.epam.esm.dao.api;

import com.epam.esm.entity.Certificate;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface CertificateDao extends Dao<Certificate>{

    List<Certificate> findFiltered(@RequestParam(required = false) String tagFieldValue,
                                   @RequestParam(required = false) String searchBy,
                                   @RequestParam(required = false) String sortBy,
                                   int offset, int pageSize);

    Certificate update(Certificate certificate);

    int createCertificateBikeGoods(long certificateId, long bikeGoodsId);

    List<Certificate> findByTagsId(List<Integer> goodsId, int tagsCount, int offset, int pageSize);

    BigDecimal findCostCertificates(List<Integer> certificatesId);
}
