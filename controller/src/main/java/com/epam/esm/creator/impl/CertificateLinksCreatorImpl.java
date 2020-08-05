package com.epam.esm.creator.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateLinksCreatorImpl implements LinksCreator<CertificateDto> {
    private static final String GET_CERTIFICATE = "getCertificate";
    private static final String DELETE_CERTIFICATE = "deleteCertificate";
    private static final String ALL_CERTIFICATES = "allCertificates";
    private static final String CREATE_CERTIFICATE = "createCertificates";
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final Validator<Certificate> validator;

    @Autowired
    public CertificateLinksCreatorImpl(Validator<Certificate> validator) {
        this.validator = validator;
    }

    @Override
    public void createForSingleEntity(CertificateDto certificateDto) {
        long certificateId = certificateDto.getId();

        if (validator.isAdmin()) {
            certificateDto.add(
                    linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withRel(DELETE_CERTIFICATE),
                    linkTo(methodOn(CertificateController.class).add(new CertificateDto())).withRel(CREATE_CERTIFICATE));
        }

        certificateDto.add(
                linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE),
                linkTo(methodOn(CertificateController.class).getFilteredList(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE,
                        null, null, null)).withRel(ALL_CERTIFICATES));
    }

    @Override
    public void createForListEntities(List<CertificateDto> certificatesDto) {
        for (CertificateDto concreteCertificateDto : certificatesDto) {
            long certificateId = concreteCertificateDto.getId();
            if (validator.isAdmin()) {
                concreteCertificateDto.add(
                        linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withRel(DELETE_CERTIFICATE));
            }
            concreteCertificateDto.add(
                    linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE));
        }
    }

    @Override
    public List<Link> createByEntityId(long certificateId) {
        List<Link> links = new ArrayList<>();

        if (validator.isAdmin()) {
            links.add(linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withSelfRel());
            links.add(linkTo(methodOn(CertificateController.class).add(new CertificateDto())).withRel(CREATE_CERTIFICATE));
        }
        links.add(linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE));
        links.add(linkTo(methodOn(CertificateController.class).getFilteredList(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE,
                null, null, null)).withRel(ALL_CERTIFICATES));

        return links;
    }
}