package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name", "price", "goodsType"})
@ToString(of = {"id", "name", "price", "goodsType"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bike_goods")
@NamedQueries({
        @NamedQuery(name = "BikeGoods.findAll", query = BikeGoods.QueryNames.FIND_ALL),
        @NamedQuery(name = "BikeGoods.findById", query = BikeGoods.QueryNames.FIND_BY_ID),
        @NamedQuery(name = "BikeGoods.lockById", query = BikeGoods.QueryNames.LOCK_BY_ID)
})
public class BikeGoods implements Identifable {

    @Id
    @SequenceGenerator(name = "bike_goods_id_seq", sequenceName = "bike_goods_id_seq", allocationSize = 1)
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
    @JoinTable(name = "certificate_bike_goods",
            joinColumns = {@JoinColumn(name = "bike_goods_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")})
    List<Certificate> certificates;


    public static final class QueryNames {
        public static final String FIND_BY_ID = "SELECT b_g FROM bike_goods b_g WHERE lock=false AND id=:goodsId";
        public static final String FIND_ALL = "SELECT b_g FROM bike_goods b_g WHERE lock=false ORDER BY id";
        public static final String LOCK_BY_ID = "UPDATE bike_goods SET lock=true WHERE id=:goodsId";

        public QueryNames() {
        }
    }
}
