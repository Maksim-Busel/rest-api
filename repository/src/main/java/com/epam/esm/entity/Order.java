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
@NamedQueries({
        @NamedQuery(name = "Order.findAll", query = Order.QueryNames.FIND_ALL),
        @NamedQuery(name = "Order.findById", query = Order.QueryNames.FIND_BY_ID),
        @NamedQuery(name = "Order.lockById", query = Order.QueryNames.LOCK_BY_ID),
        @NamedQuery(name = "Order.findAllByUserId", query = Order.QueryNames.FIND_ALL_BY_USER_ID),
})
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

    public static final class QueryNames {
        public static final String FIND_BY_ID = "SELECT o FROM orders o WHERE id=:orderId AND lock=false";
        public static final String FIND_ALL = "SELECT o FROM orders o WHERE lock=false ORDER BY id ";
        public static final String LOCK_BY_ID = "UPDATE orders SET lock=true WHERE id=:orderId";
        public static final String FIND_ALL_BY_USER_ID = "SELECT o FROM orders o WHERE user_id=:userId AND lock=false ORDER BY id";

        public QueryNames() {
        }
    }
}
