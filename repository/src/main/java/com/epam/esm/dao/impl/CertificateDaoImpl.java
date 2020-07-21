package com.epam.esm.dao.impl;

import com.epam.esm.builder.CertificateQueryBuilder;
import com.epam.esm.dao.api.CertificateDao;
import com.epam.esm.entity.Certificate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class CertificateDaoImpl implements CertificateDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CertificateQueryBuilder builder;

    public static final String USE_CERTIFICATE_BUY_BIKE_GOODS = "INSERT INTO certificate_bike_goods " +
            "(certificate_id, bike_goods_id) VALUES(:certificateId,:bikeGoodsId)";
    private static final String FIND_BY_TAGS_ID = "SELECT c.id, c.name, c.description, c.price, c.date_creation, " +
            "c.date_modification, c.duration FROM certificate c JOIN certificate_bike_goods c_b_g ON id=certificate_id" +
            " WHERE c_b_g.bike_goods_id IN :goodsId AND lock=false GROUP BY id HAVING COUNT(id)=:goodsCount ORDER BY id ";
    private static final String FIND_COST_CERTIFICATES = "SELECT SUM(price) FROM certificate WHERE lock=false " +
            "AND id IN :certificatesId";

    @Autowired
    public CertificateDaoImpl(EntityManager entityManager, CertificateQueryBuilder builder) {
        this.entityManager = entityManager;
        this.builder = builder;
    }

    @Override
    public Certificate create(Certificate certificate) {
        entityManager.persist(certificate);

        return certificate;
    }

    @Override
    public Certificate findById(long id) {
        TypedQuery<Certificate> certificateQuery = entityManager.createNamedQuery("Certificate.findById", Certificate.class);
        certificateQuery.setParameter("certificateId", id);

        return certificateQuery.getSingleResult();
    }

    @Override
    public int lockById(long id) {
        Query certificateQuery = entityManager.createNamedQuery("Certificate.lockById");
        certificateQuery.setParameter("certificateId", id);

        return certificateQuery.executeUpdate();
    }

    @Override
    public List<Certificate> findFiltered(String tagFieldValue, String searchBy, String sortBy, int offset, int pageSize) {
        String queryForFilter = builder.buildQueryForFilter(tagFieldValue, searchBy, sortBy);

        Query certificatesQuery = entityManager.createNativeQuery(queryForFilter, Certificate.class);
        certificatesQuery.setFirstResult(offset);
        certificatesQuery.setMaxResults(pageSize);

        return certificatesQuery.getResultList();
    }

    @Override
    public Certificate update(Certificate certificate) {
        entityManager.merge(certificate);

        return certificate;
    }

    @Override
    public int createCertificateBikeGoods(long certificateId, long bikeGoodsId) {
        Query certificateBikeGoodsQuery = entityManager.createNativeQuery(USE_CERTIFICATE_BUY_BIKE_GOODS);
        certificateBikeGoodsQuery.setParameter("certificateId", certificateId);
        certificateBikeGoodsQuery.setParameter("bikeGoodsId", bikeGoodsId);

        return certificateBikeGoodsQuery.executeUpdate();
    }

    @Override
    public List<Certificate> findByTagsId(List<Integer> tagsId, int tagsCount, int offset, int pageSize) {
        Session currentSession = entityManager.unwrap(Session.class);

        org.hibernate.query.Query<Certificate> certificatesQuery = currentSession.createNativeQuery(FIND_BY_TAGS_ID, Certificate.class);
        certificatesQuery.setFirstResult(offset);
        certificatesQuery.setMaxResults(pageSize);
        certificatesQuery.setParameterList("goodsId", tagsId);
        certificatesQuery.setParameter("goodsCount", tagsCount);

        return certificatesQuery.getResultList();
    }

    @Override
    public BigDecimal findCostCertificates(List<Integer> certificatesId) {
        Session currentSession = entityManager.unwrap(Session.class);

        org.hibernate.query.Query certificateQuery = currentSession.createNativeQuery(FIND_COST_CERTIFICATES);
        certificateQuery.setParameterList("certificatesId", certificatesId);

        return (BigDecimal) certificateQuery.getSingleResult();
    }

}
