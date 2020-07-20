package com.epam.esm.builder;

import org.springframework.web.bind.annotation.RequestParam;

public interface CertificateQueryBuilder {

    String buildQueryForFilter(@RequestParam(required = false) String tagFieldValue,
                               @RequestParam(required = false) String searchBy,
                               @RequestParam(required = false) String sortBy);
}
