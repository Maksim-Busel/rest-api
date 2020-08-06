package com.epam.esm.dto;

import com.epam.esm.entity.BikeGoodsType;
import com.epam.esm.entity.Identifable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeGoodsDto extends RepresentationModel<BikeGoodsDto> implements Identifable {

    private long id;
    private String name;
    private BigDecimal price;
    private BikeGoodsType goodsType;

}
