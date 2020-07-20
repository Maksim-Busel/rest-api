package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateMapperImpl implements Mapper<Certificate, CertificateDto> {
    private final ModelMapper mapper;

    @Autowired
    public CertificateMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CertificateDto convertToDto(Certificate certificate) {
        return mapper.map(certificate, CertificateDto.class);
    }

    @Override
    public Certificate convertToEntity(CertificateDto certificateDto) {
        return mapper.map(certificateDto, Certificate.class);
    }

    @Override
    public List<CertificateDto> convertAllToDto(List<Certificate> certificates) {
        return certificates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
