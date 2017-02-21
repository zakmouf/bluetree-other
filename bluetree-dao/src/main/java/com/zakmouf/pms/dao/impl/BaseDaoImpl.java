package com.zakmouf.pms.dao.impl;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDaoImpl {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected String msg(String pattern, Object... arguments) {
	return MessageFormat.format(pattern, arguments);
    }

    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Value("${global.next.id.query}")
    private String selectNextIdQuery;

    protected Long getNextId() {
	return jdbcTemplate.queryForObject(selectNextIdQuery, Long.class);
    }

}
