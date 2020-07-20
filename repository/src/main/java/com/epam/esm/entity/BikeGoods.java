package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name", "price","goodsType"})
@ToString(of = {"id","name", "price","goodsType"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bike_goods")
public class BikeGoods implements Identifable{

    @Id
    @SequenceGenerator (name="bike_goods_id_seq", sequenceName = "bike_goods_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bike_goods_id_seq")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "goods_type")
    private BikeGoodsType goodsType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (name = "certificate_bike_goods",
                joinColumns = {@JoinColumn(name = "bike_goods_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")})
    List<Certificate> certificates;
}
