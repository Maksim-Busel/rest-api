package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name","description", "price","duration","creationDate"})
@ToString(of = {"id","name","description", "price","duration","creationDate"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certificate")
@NamedQueries({
        @NamedQuery(name = Certificate.QueryNames.FIND_BY_ID,
                    query = "SELECT c FROM certificate c WHERE id=:certificateId AND lock=false"),
        @NamedQuery(name = Certificate.QueryNames.FIND_BY_NAME,
                query = "SELECT c FROM certificate c WHERE name=:certificateName"),
        @NamedQuery(name = Certificate.QueryNames.LOCK_BY_ID,
                    query = "UPDATE certificate SET lock=true WHERE id=:certificateId"),
})
public class Certificate implements Identifable {

    @Id
    @SequenceGenerator(name = "certificate_id_seq", sequenceName = "certificate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_id_seq")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration", nullable = false)
    private CertificateDuration duration;

    @Column(name = "date_creation")
    private LocalDate creationDate;

    @Column(name = "date_modification")
    private LocalDate modificationDate;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<BikeGoods> goods;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    private List<Order> orders;

    public void addGoods(BikeGoods goods){
        this.goods.add(goods);
        goods.getCertificates().add(this);
    }

    public void addOrder(Order order){
        this.orders.add(order);
        order.getCertificates().add(this);
    }


    public static final class QueryNames {
        public static final String FIND_BY_ID = "Certificate.findById";
        public static final String FIND_BY_NAME = "Certificate.findByName";
        public static final String LOCK_BY_ID = "Certificate.lockById";

        public QueryNames() {
        }
    }
}
