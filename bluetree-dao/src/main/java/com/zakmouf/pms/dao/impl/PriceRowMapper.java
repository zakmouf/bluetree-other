package com.zakmouf.pms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zakmouf.pms.domain.Price;

public class PriceRowMapper extends BaseRowMapper implements RowMapper<Price> {

    @Override
    public Price mapRow(ResultSet rs, int rowNum) throws SQLException {
	Price price = mapPrice(rs);
	return price;
    }

}
