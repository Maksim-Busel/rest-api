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
        @NamedQuery(name = Order.QueryNames.FIND_ALL,
                    query = "SELECT o FROM orders o WHERE lock=false ORDER BY id "),
        @NamedQuery(name = Order.QueryNames.FIND_BY_ID,
                    query = "SELECT o FROM orders o WHERE id=:orderId AND lock=false"),
        @NamedQuery(name = Order.QueryNames.LOCK_BY_ID,
                    query = "UPDATE orders SET lock=true WHERE id=:orderId"),
        @NamedQuery(name = Order.QueryNames.FIND_ALL_BY_USER_ID,
                    query = "SELECT o FROM orders o WHERE user_id=:userId AND lock=false ORDER BY id"),
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
    private List<Certificate> certificates;

    public void addCertificate(Certificate certificate){
        this.certificates.add(certificate);
        certificate.getOrders().add(this);
    }

    public void setUserId(long userId){
        user.setId(userId);
    }

    public static final class QueryNames {
        public static final String FIND_BY_ID = "Order.findById";
        public static final String FIND_ALL = "Order.findAll";
        public static final String LOCK_BY_ID = "Order.lockById";
        public static final String FIND_ALL_BY_USER_ID = "Order.findAllByUserId";

        public QueryNames() {
        }
    }
}
