package com.epam.esm.service.api;

import com.epam.esm.entity.BikeGoods;

import java.util.List;

public interface BikeGoodsService extends Service<BikeGoods>{

    BikeGoods add(BikeGoods goods);

    BikeGoods getByName(String goodsName, boolean exceptionIfNotFound);

    BikeGoods getByName(String goodsName);

    List<BikeGoods> getAll(int pageNumber, int pageSize);

    BikeGoods edit(BikeGoods updatedBikeGoods);
}
