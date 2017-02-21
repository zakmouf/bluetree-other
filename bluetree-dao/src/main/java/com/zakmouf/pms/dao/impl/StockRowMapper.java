package com.zakmouf.pms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zakmouf.pms.domain.Stock;

public class StockRowMapper extends BaseRowMapper implements RowMapper<Stock> {

    @Override
    public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
	Stock stock = mapStock(rs);
	return stock;
    }

}
