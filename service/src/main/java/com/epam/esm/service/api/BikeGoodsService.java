package com.epam.esm.service.api;

import com.epam.esm.entity.BikeGoods;

import java.util.List;

public interface BikeGoodsService extends Service<BikeGoods>{

    BikeGoods add(BikeGoods goods);

    List<BikeGoods> getAll(int pageNumber, int pageSize);

}
