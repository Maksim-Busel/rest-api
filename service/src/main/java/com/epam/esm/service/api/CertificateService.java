package com.epam.esm.service.api;

import com.epam.esm.entity.Certificate;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface CertificateService extends Service<Certificate>{

    Certificate add(Certificate certificate);

    List<Certificate> getFilteredList(@RequestParam (required = false) String tagFieldValue,
                                      @RequestParam (required = false) String searchBy,
                                      @RequestParam (required = false) String sortBy,
                                      int pageNumber, int pageSize);

    Certificate edit(Certificate certificate);

    void useCertificateBuyBikeGoods(long certificateId, long[] bikeGoodsId);

    Certificate editPart(Certificate certificate);

    BigDecimal getCostCertificates(List<Integer> certificatesId);

    List<Certificate> findByTagsId(List<Integer> goodsId, int pageNumber, int pageSize);
}
