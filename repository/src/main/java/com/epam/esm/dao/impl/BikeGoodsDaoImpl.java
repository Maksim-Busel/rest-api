package com.epam.esm.dao.impl;

import com.epam.esm.dao.api.BikeGoodsDao;
import com.epam.esm.entity.BikeGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BikeGoodsDaoImpl extends AbstractDao<BikeGoods> implements BikeGoodsDao {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public BikeGoodsDaoImpl(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public BikeGoods findById(long id) {
        TypedQuery<BikeGoods> goodsQuery = entityManager.createNamedQuery(BikeGoods.QueryNames.FIND_BY_ID, BikeGoods.class);
        goodsQuery.setParameter("goodsId", id);

        return goodsQuery.getSingleResult();
    }

    @Override
    public BikeGoods findByName(String goodsName) {
        TypedQuery<BikeGoods> goodsQuery = entityManager.createNamedQuery(BikeGoods.QueryNames.FIND_BY_NAME, BikeGoods.class);
        goodsQuery.setParameter("goodsName", goodsName);

        return goodsQuery.getSingleResult();
    }

    @Override
    public List<BikeGoods> findAll(int offset, int pageSize) {
        TypedQuery<BikeGoods> goodsQuery = entityManager.createNamedQuery(BikeGoods.QueryNames.FIND_ALL, BikeGoods.class);
        goodsQuery.setFirstResult(offset);
        goodsQuery.setMaxResults(pageSize);

        return goodsQuery.getResultList();
    }

    @Override
    public int lockById(long id) {
        Query goodsQuery = entityManager.createNamedQuery(BikeGoods.QueryNames.LOCK_BY_ID);
        goodsQuery.setParameter("goodsId", id);

        return goodsQuery.executeUpdate();
    }
}
