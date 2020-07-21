package com.epam.esm.builder.impl;

import com.epam.esm.builder.CertificateQueryBuilder;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class CertificateQueryBuilderImpl implements CertificateQueryBuilder {
    private static final String CERTIFICATE = " c.";
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String FIND_BY_TAG_FIELD = " JOIN certificate_bike_goods c_b_g " +
            "ON c.id=c_b_g.certificate_id JOIN bike_goods b_g ON " +
            "c_b_g.bike_goods_id=b_g.id WHERE b_g.";
    private static final String BIKE_GOODS_UNLOCK = "b_g.lock=false ";
    private static final String LIKE = " LIKE '%";
    private static final String FIELD_GOODS_TYPE = "goods_type=";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String ID = "id";
    private static final String FIND_ALL_DISTINCT_FROM_CERTIFICATES = "SELECT DISTINCT c.id, c.name, c.description, " +
            "c.price, c.date_creation, c.date_modification, c.duration FROM certificate c";
    private static final String CERTIFICATE_UNLOCK = " c.lock=false ";
    private static final String EMPTY_STRING = "";

    @Override
    public String buildQueryForFilter(String tagFieldValue, String searchBy, String sortBy) {
        StringBuilder filterQuery = new StringBuilder(FIND_ALL_DISTINCT_FROM_CERTIFICATES);

        filterQuery.append(buildQueryForTag(tagFieldValue))
                .append(buildQueryForSearch(searchBy))
                .append(CERTIFICATE_UNLOCK)
                .append(buildQueryForSort(sortBy));

        return filterQuery.toString();
    }

    private StringBuilder buildQueryForTag(String bikeGoodsFieldValue) {
        StringBuilder tagQuery = new StringBuilder();

        if (isNotBlank(bikeGoodsFieldValue)) {
            tagQuery.append(FIND_BY_TAG_FIELD);

            if (bikeGoodsFieldValue.contains(FIELD_GOODS_TYPE)) {
                String valueGoodsType = "'" + bikeGoodsFieldValue.replace(FIELD_GOODS_TYPE, EMPTY_STRING).trim().toUpperCase() + "'";
                tagQuery.append(FIELD_GOODS_TYPE)
                        .append(valueGoodsType);
            } else {
                tagQuery.append(bikeGoodsFieldValue.trim());
            }

            tagQuery.append(AND)
                    .append(BIKE_GOODS_UNLOCK)
                    .append(AND);
        } else {
            tagQuery.append(WHERE);
        }

        return tagQuery;
    }

    private StringBuilder buildQueryForSearch(String searchBy) {
        StringBuilder searchQuery = new StringBuilder();

        if (isNotBlank(searchBy)) {
            String[] fieldAndValue = searchBy.trim().split("=");
            String fieldForSearch = fieldAndValue[0].trim();
            String valueForSearch = fieldAndValue[1].trim();

            searchQuery.append(CERTIFICATE)
                    .append(fieldForSearch)
                    .append(LIKE)
                    .append(valueForSearch)
                    .append("%'")
                    .append(AND);
        }

        return searchQuery;
    }

    private StringBuilder buildQueryForSort(String sortBy) {
        StringBuilder orderQuery = new StringBuilder();

        if (isNotBlank(sortBy)) {
            orderQuery.append(ORDER_BY)
                    .append(CERTIFICATE)
                    .append(sortBy.trim());
        } else {
            orderQuery.append(ORDER_BY).append(ID);
        }

        return orderQuery;
    }
}
