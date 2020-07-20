package com.epam.esm.dto;

import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.entity.Identifable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeGoodsDto extends RepresentationModel<BikeGoodsDto> implements Identifable {

    private long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5000")
    private BigDecimal price;

    @NotNull
    private BikeGoodsType goodsType;

}
