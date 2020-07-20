package com.epam.esm.dao.api;

import com.epam.esm.entity.BikeGoods;

import java.util.List;

public interface BikeGoodsDao extends Dao<BikeGoods>{

    List<BikeGoods> findAll(int offset, int pageSize);

}
