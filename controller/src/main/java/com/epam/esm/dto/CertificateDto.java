package com.epam.esm.dto;

import com.epam.esm.entity.CertificateDuration;
import com.epam.esm.entity.Identifable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto extends RepresentationModel<CertificateDto> implements Identifable {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate dateCreation;
    private LocalDate dateModification;
    private CertificateDuration duration;

}
