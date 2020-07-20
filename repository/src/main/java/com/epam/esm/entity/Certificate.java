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

}
