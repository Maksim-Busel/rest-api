package esm.dao;//package com.epam.esm.dao;
//
//import com.epam.esm.RestApplication;
//import com.epam.esm.dao.api.BikeGoodsDao;
//import com.epam.esm.entity.BikeGoods;
//import com.epam.esm.entity.BikeGoodsType;
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
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//@ContextConfiguration(classes = {RestApplication.class})
//@SqlGroup({
//        @Sql("/bike_goods-table.sql"),
//        @Sql("/bike_goods-data.sql"),
//
//        @Sql(scripts = "/bike_goods-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//public class BikeGoodsDaoIT {
//    @Autowired
//    private BikeGoodsDao dao;
//    @Autowired
//    private JdbcTemplate template;
//
//    private final static String TABLE_NAME = "bike_goods";
//
//    @Test
//    public void createWhenBikeGoodsCorrectShouldAdd() {
//        JdbcTestUtils.deleteFromTables(template, TABLE_NAME);
//
//        BikeGoods expected = new BikeGoods();
//        expected.setName("Goods");
//        expected.setPrice(new BigDecimal(234.32));
//        expected.setGoodsType(BikeGoodsType.BIKE);
//
//        dao.create(expected);
//
//        assertThat(JdbcTestUtils.countRowsInTable(template, TABLE_NAME), is(1));
//    }
//
//    @Test
//    public void findByIdWhenExistSuchBikeGoodsShouldReturnHis() {
//        long id = 1;
//        BigDecimal expectedPrice = new BigDecimal(450);
//        String expectedName = "Aist";
//        BikeGoodsType expectedType = BikeGoodsType.BIKE;
//
//        BikeGoods actual = dao.findById(id);
//
//        assertThat(actual.getId(), is(id));
//        assertThat(actual.getPrice(), is(expectedPrice));
//        assertThat(actual.getName(), is(expectedName));
//        assertThat(actual.getGoodsType(), is(expectedType));
//    }
//
//    @Test
//    public void findAllWhenHasBikeGoodsShouldReturnAll() {
//        int expectedSize = 5;
//
//        int actualSize = dao.findAll().size();
//
//        assertThat(actualSize, is(expectedSize));
//    }
//
//    @Test
//    public void lockByIdWhenExistSuchBikeGoodsShouldMarkItAsLockAndNotReturnUponRequest() {
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
//}