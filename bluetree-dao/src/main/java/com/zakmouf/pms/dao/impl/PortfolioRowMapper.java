package com.zakmouf.pms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.zakmouf.pms.domain.Portfolio;

public class PortfolioRowMapper extends BaseRowMapper implements
	RowMapper<Portfolio> {

    @Override
    public Portfolio mapRow(ResultSet rs, int rowNum) throws SQLException {
	Portfolio portfolio = mapPortfolio(rs);
	portfolio.setIndice(mapStock(rs, "indice"));
	return portfolio;
    }

}
