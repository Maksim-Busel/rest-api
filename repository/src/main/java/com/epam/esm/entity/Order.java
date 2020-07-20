package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order implements Identifable {

    @Id
    @SequenceGenerator(name="order_id_seq", sequenceName = "order_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    private long id;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "price_total")
    private BigDecimal priceTotal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable (name = "certificate_orders",
                joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")})
    List<Certificate> certificates;
}
