package esm.dao;//package com.epam.esm.dao;
//
//import com.epam.esm.RestApplication;
//import com.epam.esm.dao.api.CertificateDao;
//import com.epam.esm.entity.Certificate;
//import com.epam.esm.entity.CertificateDuration;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.jdbc.SqlGroup;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//@ContextConfiguration(classes = {RestApplication.class})
//@SqlGroup({
//        @Sql("/certificate-table.sql"),
//        @Sql("/certificate-data.sql"),
//
//        @Sql(value = "/certificate-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//public class CertificateDaoIT {
//    @Autowired
//    private CertificateDao dao;
//    @Autowired
//    private JdbcTemplate template;
//
//    private final static String TABLE_NAME = "certificate";
//
//    @Test
//    public void createWhenCertificateCorrectShouldAdd() {
//        JdbcTestUtils.deleteFromTables(template, TABLE_NAME);
//
//        Certificate expected = new Certificate();
//        expected.setId(1);
//        expected.setName("certificate");
//        expected.setDescription("good certificate");
//        expected.setPrice(new BigDecimal("234.32"));
//        expected.setDateCreation(LocalDate.now());
//        expected.setDuration(CertificateDuration.SIX_MONTH);
//
//        dao.create(expected);
//
//        assertThat(JdbcTestUtils.countRowsInTable(template, TABLE_NAME), is(1));
//    }
//
//    @Test
//    public void findByIdWhenExistSuchCertificateShouldReturnHis() {
//        long id = 1;
//        String expectedName = "certificate";
//        String expectedDescription = "test";
//        BigDecimal expectedPrice = new BigDecimal(300);
//        LocalDate expectedDateCreation = LocalDate.parse("2020-07-16");
//        CertificateDuration expectedDuration = CertificateDuration.ONE_MONTH;
//
//        Certificate actual = dao.findById(id);
//
//        assertThat(actual.getId(), is(id));
//        assertThat(actual.getName(), is(expectedName));
//        assertThat(actual.getDescription(), is(expectedDescription));
//        assertThat(actual.getPrice(), is(expectedPrice));
//        assertThat(actual.getDateCreation(), is(expectedDateCreation));
//        assertThat(actual.getDuration(), is(expectedDuration));
//    }
//
//    @Test
//    public void findAllWhenHasCertificatesShouldReturnAll() {
//        int expectedSize = 5;
//
//        int actualSize = dao.findAll().size();
//
//        assertThat(actualSize, is(expectedSize));
//    }
//
//    @Test
//    public void lockByIdWhenExistSuchCertificateShouldMarkItAsLockAndNotReturnUponRequest() {
//        long idForLock = 1;
//        int realSize = 5;
//        int expectedAvailableGoods = 4;
//
//        dao.lockById(idForLock);
//        int numberAvailableGoods = dao.findAll().size();
//
//        assertThat(JdbcTestUtils.countRowsInTable(template, TABLE_NAME), is(realSize));
//        assertThat(numberAvailableGoods, is(expectedAvailableGoods));
//    }
//
//    @Test
//    public void updateWhenExistSuchCertificateShouldChangeHisAndSetDateModificationButNoChangeDateCreation() {
//        long idForUpdate = 1;
//        LocalDate expectedDateCreation = LocalDate.parse("2020-07-16");
//
//        Certificate expected = new Certificate();
//        expected.setId(idForUpdate);
//        expected.setName("certificate");
//        expected.setDescription("good certificate");
//        expected.setPrice(new BigDecimal("234.32"));
//        expected.setDateModification(LocalDate.now());
//        expected.setDuration(CertificateDuration.SIX_MONTH);
//
//        dao.update(expected);
//        Certificate actual = dao.findById(idForUpdate);
//
//        assertThat(actual.getId(), is(expected.getId()));
//        assertThat(actual.getName(), is(expected.getName()));
//        assertThat(actual.getDescription(), is(expected.getDescription()));
//        assertThat(actual.getPrice(), is(expected.getPrice()));
//        assertThat(actual.getDateCreation(), is(expectedDateCreation));
//        assertThat(actual.getDateModification(), is(expected.getDateModification()));
//        assertThat(actual.getDuration(), is(expected.getDuration()));
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql("/bike_goods-table.sql"),
//            @Sql("/bike_goods-data.sql"),
//            @Sql("/certificate-table.sql"),
//            @Sql("/certificate-data.sql"),
//            @Sql("/certificate_bike_goods-table.sql"),
//
//            @Sql(scripts = "/certificate_bike_goods-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//            @Sql(scripts = "/bike_goods-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//            @Sql(value = "/certificate-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//
//    })
//    public void createCertificateBikeGoodsWhenNotExistSoPairShouldAddInCertificateBikeGoodsTable() {
//        String tableName = "certificate_bike_goods";
//        long certificateId = 1;
//        long bikeGoodsId =1;
//
//        dao.createCertificateBikeGoods(certificateId, bikeGoodsId);
//
//        assertThat(JdbcTestUtils.countRowsInTable(template, tableName), is(1));
//    }
//
//    @Test
//    @SqlGroup({
//            @Sql("/bike_goods-table.sql"),
//            @Sql("/bike_goods-data.sql"),
//            @Sql("/certificate-table.sql"),
//            @Sql("/certificate-data.sql"),
//            @Sql("/certificate_bike_goods-table.sql"),
//            @Sql("/certificate_bike_goods-data.sql"),
//
//            @Sql(scripts = "/certificate_bike_goods-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//            @Sql(scripts = "/bike_goods-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//            @Sql(value = "/certificate-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//
//    })
//    public void findFilteredWhenPassThreeParametersShouldReturnAllAppropriate() {
//        String searchBy = "name=cert";
//        String sortBy = "price";
//        String tag = "goods_type=BIKE";
//        int expectedSize = 3;
//
//        int actualSize = dao.findFiltered(sortBy, searchBy, tag).size();
//
//        assertThat(actualSize, is(expectedSize));
//    }
//}