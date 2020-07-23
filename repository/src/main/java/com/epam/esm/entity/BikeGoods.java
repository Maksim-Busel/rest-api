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
        @NamedQuery(name = BikeGoods.QueryNames.FIND_ALL,
                    query = "SELECT b_g FROM bike_goods b_g WHERE lock=false ORDER BY id"),
        @NamedQuery(name = BikeGoods.QueryNames.FIND_BY_ID,
                    query = "SELECT b_g FROM bike_goods b_g WHERE lock=false AND id=:goodsId"),
        @NamedQuery(name = BikeGoods.QueryNames.FIND_BY_NAME,
                    query = "SELECT b_g FROM bike_goods b_g WHERE name=:goodsName"),
        @NamedQuery(name = BikeGoods.QueryNames.LOCK_BY_ID,
                    query = "UPDATE bike_goods SET lock=true WHERE id=:goodsId")
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
    private List<Certificate> certificates;


    public void addCertificate(Certificate certificate){
        this.certificates.add(certificate);
        certificate.getGoods().add(this);
    }

    public static final class QueryNames {
        public static final String FIND_BY_ID = "BikeGoods.findById";
        public static final String FIND_BY_NAME = "BikeGoods.findByName";
        public static final String FIND_ALL = "BikeGoods.findAll";
        public static final String LOCK_BY_ID = "BikeGoods.lockById";

        public QueryNames() {
        }
    }
}
