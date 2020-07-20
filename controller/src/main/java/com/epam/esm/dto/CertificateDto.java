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

    @NotBlank
    @Size(min = 2, max = 70)
    private String name;

    @NotBlank
    @Size(min = 2, max = 120)
    private String description;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5000")
    private BigDecimal price;

    @Null
    private LocalDate dateCreation;

    @Null
    private LocalDate dateModification;

    @NotNull
    private CertificateDuration duration;

}
