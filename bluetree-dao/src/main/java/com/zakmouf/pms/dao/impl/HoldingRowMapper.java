package com.zakmouf.pms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zakmouf.pms.domain.Holding;

public class HoldingRowMapper extends BaseRowMapper implements
	RowMapper<Holding> {

    @Override
    public Holding mapRow(ResultSet rs, int rowNum) throws SQLException {
	Holding holding = mapHolding(rs);
	holding.setStock(mapStock(rs));
	return holding;
    }

}
