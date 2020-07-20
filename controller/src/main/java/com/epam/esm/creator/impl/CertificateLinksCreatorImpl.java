package com.epam.esm.creator.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.CertificateDto;
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

    @Override
    public CertificateDto createForSingleEntity(CertificateDto certificateDto) {
        long certificateId = certificateDto.getId();
        certificateDto.add(
                linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE),
                linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withRel(DELETE_CERTIFICATE),
                linkTo(methodOn(CertificateController.class).getFilteredList(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE,
                        null, null, null)).withRel(ALL_CERTIFICATES),
                linkTo(methodOn(CertificateController.class).add(new CertificateDto())).withRel(CREATE_CERTIFICATE)
        );

        return certificateDto;
    }

    @Override
    public List<CertificateDto> createForListEntities(List<CertificateDto> certificatesDto) {
        for (CertificateDto concreteCertificateDto : certificatesDto) {
            long certificateId = concreteCertificateDto.getId();
            concreteCertificateDto.add(
                    linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE),
                    linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withRel(DELETE_CERTIFICATE)
            );
        }

        return certificatesDto;
    }

    @Override
    public List<Link> createByEntityId(long certificateId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CertificateController.class).deleteById(certificateId)).withRel(DELETE_CERTIFICATE));
        links.add(linkTo(methodOn(CertificateController.class).getById(certificateId)).withRel(GET_CERTIFICATE));
        links.add(linkTo(methodOn(CertificateController.class).add(new CertificateDto())).withRel(CREATE_CERTIFICATE));
        links.add(linkTo(methodOn(CertificateController.class).getFilteredList(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE,
                null, null, null)).withRel(ALL_CERTIFICATES));

        return links;
    }
}