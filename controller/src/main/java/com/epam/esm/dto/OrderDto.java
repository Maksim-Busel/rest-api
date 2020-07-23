package com.epam.esm.dto;

import com.epam.esm.entity.Identifable;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> implements Identifable {

    private long id;
    private long userId;
    private LocalDate orderDate;
    private BigDecimal priceTotal;
    private List<Integer> certificatesId;
}
