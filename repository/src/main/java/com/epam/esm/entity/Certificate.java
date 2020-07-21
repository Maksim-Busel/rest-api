package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"name","description", "price","duration","dateCreation"})
@ToString(of = {"id","name","description", "price","duration","dateCreation"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certificate")
@NamedQueries({
        @NamedQuery(name = "Certificate.findById", query = Certificate.QueryNames.FIND_BY_ID),
        @NamedQuery(name = "Certificate.lockById", query = Certificate.QueryNames.LOCK_BY_ID),
        @NamedQuery(name = "Certificate.findCostCertificates", query = Certificate.QueryNames.FIND_COST_CERTIFICATES),
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
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    List<BikeGoods> goods;

    @ManyToMany(mappedBy = "certificates", fetch = FetchType.LAZY)
    List<Order> orders;


    public static final class QueryNames {
        public static final String FIND_BY_ID = "SELECT c FROM certificate c WHERE id=:certificateId AND lock=false";
        public static final String LOCK_BY_ID = "UPDATE certificate SET lock=true WHERE id=:certificateId";
        public static final String FIND_COST_CERTIFICATES = "SELECT SUM(price) FROM certificate WHERE lock=false " +
                "AND id IN :certificatesId";

        public QueryNames() {
        }
    }
}
