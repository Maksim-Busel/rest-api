package com.epam.esm.controller;

import com.epam.esm.creator.LinksCreator;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.mapper.Mapper;
import com.epam.esm.service.api.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final CertificateService service;
    private final Mapper<Certificate, CertificateDto> mapper;
    private final LinksCreator<CertificateDto> linksCreator;

    @Autowired
    public CertificateController(CertificateService service, Mapper<Certificate, CertificateDto> mapper, LinksCreator<CertificateDto> linksCreator) {
        this.service = service;
        this.mapper = mapper;
        this.linksCreator = linksCreator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto add(@RequestBody CertificateDto certificateDto) {
        Certificate certificate = mapper.convertToEntity(certificateDto);

        Certificate addedCertificate = service.add(certificate);
        CertificateDto certificateDtoFromDb = mapper.convertToDto(addedCertificate);

        linksCreator.createForSingleEntity(certificateDtoFromDb);
        certificateDtoFromDb.add(linkTo(methodOn(CertificateController.class).add(certificateDto)).withSelfRel());

        return certificateDtoFromDb;
    }

    @GetMapping("/info/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CertificateDto getById(@PathVariable long id) {
        Certificate certificate = service.getById(id);
        CertificateDto certificateDto = mapper.convertToDto(certificate);

        linksCreator.createForSingleEntity(certificateDto);
        certificateDto.add(linkTo(methodOn(CertificateController.class).getById(id)).withSelfRel());

        return certificateDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Link> deleteById(@PathVariable long id) {
        service.lock(id);

        return linksCreator.createByEntityId(id);
    }

    @PostMapping("/{certificateId}/goods-management")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Link> use(@PathVariable long certificateId, @RequestParam long[] bikeGoodsId) {
        service.useCertificateBuyBikeGoods(certificateId, bikeGoodsId);

        return linksCreator.createByEntityId(certificateId);
    }


    @GetMapping("/info/filter")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<CertificateDto> getFilteredList(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                           @RequestParam(required = false) String tagFieldValue,
                                                           @RequestParam(required = false) String searchBy,
                                                           @RequestParam(required = false) String sortBy) {

        List<Certificate> certificates = service.getFilteredList(tagFieldValue, searchBy, sortBy, pageNumber, pageSize);
        List<CertificateDto> certificatesDto = mapper.convertAllToDto(certificates);

        linksCreator.createForListEntities(certificatesDto);
        Link selfLink = linkTo(methodOn(CertificateController.class)
                .getFilteredList(pageNumber, pageSize, tagFieldValue, searchBy, sortBy)).withSelfRel();

        return CollectionModel.of(certificatesDto, selfLink);
    }

    @PutMapping("/{certificateId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto edit(@RequestBody CertificateDto certificateDto, @PathVariable long certificateId) {
        Certificate certificate = mapper.convertToEntity(certificateDto);
        certificate.setId(certificateId);

        Certificate editedCertificate = service.edit(certificate);
        CertificateDto certificateDtoFromDb = mapper.convertToDto(editedCertificate);

        linksCreator.createForSingleEntity(certificateDtoFromDb);
        certificateDtoFromDb.add(linkTo(methodOn(CertificateController.class).edit(certificateDto, certificateId)).withSelfRel());

        return certificateDtoFromDb;
    }

    @PatchMapping("/{certificateId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto editPart(@RequestBody CertificateDto certificateDto, @PathVariable long certificateId) {
        Certificate certificate = mapper.convertToEntity(certificateDto);
        certificate.setId(certificateId);

        Certificate editedCertificate = service.editPart(certificate);
        CertificateDto certificateDtoFromDb = mapper.convertToDto(editedCertificate);

        linksCreator.createForSingleEntity(certificateDtoFromDb);
        certificateDtoFromDb.add(linkTo(methodOn(CertificateController.class).editPart(certificateDto, certificateId)).withSelfRel());

        return certificateDtoFromDb;
    }

    @GetMapping("/info/filter-by-tags")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<CertificateDto> getByTagsId(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                       @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                       @RequestParam List<Integer> goodsId) {

        List<Certificate> certificates = service.findByTagsId(goodsId, pageNumber, pageSize);
        List<CertificateDto> certificatesDto = mapper.convertAllToDto(certificates);

        linksCreator.createForListEntities(certificatesDto);
        Link selfLink = linkTo(methodOn(CertificateController.class).getByTagsId(pageNumber, pageSize, goodsId)).withSelfRel();

        return CollectionModel.of(certificatesDto, selfLink);
    }
}
