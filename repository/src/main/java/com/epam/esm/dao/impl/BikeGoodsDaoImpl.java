package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BikeGoodsDaoImpl implements BikeGoodsDao {
    @PersistenceContext
    private  final EntityManager entityManager;

    private static final String FIND_BY_ID = "SELECT * FROM bike_goods WHERE lock=false AND id=:goodsId";
    private static final String FIND_ALL = "SELECT * FROM bike_goods WHERE lock=false ORDER BY id";
    private static final String LOCK_BY_ID = "UPDATE bike_goods SET lock=true WHERE id=:goodsId";


    @Autowired
    public BikeGoodsDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BikeGoods create(BikeGoods goods) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.save(goods);

       return goods;
    }

    @Override
    public BikeGoods findById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<BikeGoods> goodsQuery = currentSession.createNativeQuery(FIND_BY_ID, BikeGoods.class);
        goodsQuery.setParameter("goodsId", id);

        return goodsQuery.getSingleResult();
    }

    @Override
    public List<BikeGoods> findAll(int offset, int pageSize) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<BikeGoods> goodsQuery = currentSession.createNativeQuery(FIND_ALL, BikeGoods.class);
        goodsQuery.setFirstResult(offset);
        goodsQuery.setMaxResults(pageSize);

        return goodsQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Session currentSession = entityManager.unwrap(Session.class);

        Query goodsQuery = currentSession.createNativeQuery(LOCK_BY_ID);
        goodsQuery.setParameter("goodsId", id);

        return goodsQuery.executeUpdate();
    }
}
