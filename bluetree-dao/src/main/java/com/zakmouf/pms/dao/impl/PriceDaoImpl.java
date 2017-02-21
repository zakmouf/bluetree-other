package com.zakmouf.pms.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.zakmouf.pms.dao.PriceDao;
import com.zakmouf.pms.domain.Price;
import com.zakmouf.pms.domain.Stock;

@Repository("priceDao")
public class PriceDaoImpl extends BaseDaoImpl implements PriceDao {

    @Value("${price.select.all.query}")
    private String selectAllQuery;

    @Value("${price.insert.query}")
    private String insertQuery;

    @Value("${price.delete.all.query}")
    private String deleteAllQuery;

    @Value("${price.delete.query}")
    private String deleteQuery;

    @Value("${price.select.last.query}")
    private String selectLastQuery;

    @Value("${price.select.from.query}")
    private String selectFromQuery;

    @Value("${price.select.from.inclusive.query}")
    private String selectFromInclusiveQuery;

    @Value("${price.select.between.query}")
    private String selectBetweenQuery;

    @Value("${price.select.between.inclusive.query}")
    private String selectBetweenInclusiveQuery;

    @Override
    public List<Price> findAll(Stock stock) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Object[] args = { stock.getId() };
	int[] argTypes = { Types.NUMERIC };
	List<Price> prices = jdbcTemplate.query(selectAllQuery, args, argTypes,
		new PriceRowMapper());
	Collections.sort(prices);
	logger.debug(msg("find [{0}] prices for stock=[{1}]", prices.size(),
		stock));
	return prices;

    }

    @Override
    public void insert(Stock stock, Price price) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(price);
	Assert.notNull(price.getDate());
	Assert.notNull(price.getValue());
	Object[] args = { stock.getId(), price.getDate(), price.getValue() };
	int[] argTypes = { Types.NUMERIC, Types.DATE, Types.NUMERIC };
	jdbcTemplate.update(insertQuery, args, argTypes);
	logger.debug(msg("insert price=[{0}] in stock=[{1}]", price, stock));
    }

    @Override
    public void deleteAll(Stock stock) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Object[] args = { stock.getId() };
	int[] argTypes = { Types.NUMERIC };
	jdbcTemplate.update(deleteAllQuery, args, argTypes);
	logger.debug(msg("delete all prices from stock=[{0}]", stock));
    }

    @Override
    public void delete(Stock stock, Price price) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(price);
	Assert.notNull(price.getDate());
	Object[] args = { stock.getId(), price.getDate() };
	int[] argTypes = { Types.NUMERIC, Types.DATE };
	jdbcTemplate.update(deleteQuery, args, argTypes);
	logger.debug(msg("delete price=[{0}] from stock=[{1}]", price, stock));
    }

    @Override
    public List<Price> findFrom(Stock stock, Date fromDate) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(fromDate);
	Object[] args = { stock.getId(), fromDate };
	int[] argTypes = { Types.NUMERIC, Types.DATE };
	List<Price> prices = jdbcTemplate.query(selectFromQuery, args,
		argTypes, new PriceRowMapper());
	Collections.sort(prices);
	logger.debug(msg(
		"find [{0}] prices from=[{1,date,yyyy-MM-dd}] for stock=[{2}]",
		prices.size(), fromDate, stock));
	return prices;
    }

    @Override
    public List<Price> findFromInclusive(Stock stock, Date fromDate) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(fromDate);
	Object[] args = { stock.getId(), stock.getId(), fromDate };
	int[] argTypes = { Types.NUMERIC, Types.NUMERIC, Types.DATE };
	List<Price> prices = jdbcTemplate.query(selectFromInclusiveQuery, args,
		argTypes, new PriceRowMapper());
	Collections.sort(prices);
	logger.debug(msg(
		"find [{0}] prices inclusive from=[{1,date,yyyy-MM-dd}] for stock=[{2}]",
		prices.size(), fromDate, stock));
	return prices;
    }

    @Override
    public List<Price> findBetween(Stock stock, Date fromDate, Date toDate) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(fromDate);
	Assert.notNull(toDate);
	Object[] args = { stock.getId(), fromDate, toDate };
	int[] argTypes = { Types.NUMERIC, Types.DATE, Types.DATE };
	List<Price> prices = jdbcTemplate.query(selectBetweenQuery, args,
		argTypes, new PriceRowMapper());
	Collections.sort(prices);
	logger.debug(msg(
		"find [{0}] prices from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] for stock=[{3}]",
		prices.size(), fromDate, toDate, stock));
	return prices;
    }

    @Override
    public List<Price> findBetweenInclusive(Stock stock, Date fromDate,
	    Date toDate) {
	Assert.notNull(stock);
	Assert.notNull(stock.getId());
	Assert.notNull(fromDate);
	Assert.notNull(toDate);
	Object[] args = { stock.getId(), stock.getId(), fromDate,
		stock.getId(), toDate };
	int[] argTypes = { Types.NUMERIC, Types.NUMERIC, Types.DATE,
		Types.NUMERIC, Types.DATE };
	List<Price> prices = jdbcTemplate.query(selectBetweenInclusiveQuery,
		args, argTypes, new PriceRowMapper());
	Collections.sort(prices);
	logger.debug(msg(
		"find [{0}] prices inclusive from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] for stock=[{3}]",
		prices.size(), fromDate, toDate, stock));
	return prices;
    }

    @Value("${price.load.url.pattern}")
    private String loadUrlPattern;

    @Override
    public List<Price> loadBetween(Stock stock, Date fromDate, Date toDate) {

	Assert.notNull(stock);
	Assert.notNull(stock.getName());
	Assert.notNull(fromDate);
	Assert.notNull(toDate);

	Calendar fromCalendar = Calendar.getInstance();
	fromCalendar.setTime(fromDate);
	Calendar toCalendar = Calendar.getInstance();
	toCalendar.setTime(toDate);
	String url = msg(loadUrlPattern, stock.getName(),
		fromCalendar.get(Calendar.MONTH),
		fromCalendar.get(Calendar.DATE),
		fromCalendar.get(Calendar.YEAR),
		toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DATE),
		toCalendar.get(Calendar.YEAR));

	InputStream input = null;
	List<String> lines = new ArrayList<String>();
	try {
	    input = new URL(url).openStream();
	    InputStreamReader inputReader = new InputStreamReader(input);
	    BufferedReader buffReader = new BufferedReader(inputReader);
	    String line = buffReader.readLine();
	    while (line != null) {
		lines.add(line);
		line = buffReader.readLine();
	    }
	    lines = lines.subList(1, lines.size());
	} catch (IOException ex) {
	    logger.warn(msg("failed to open url=[{0}]", url));
	} finally {
	    try {
		if (input != null) {
		    input.close();
		}
	    } catch (IOException ex) {
		// ignore
	    }
	}

	List<Price> prices = new ArrayList<Price>();

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	for (String line : lines) {

	    String[] tokens = StringUtils.delimitedListToStringArray(line, ",");

	    Price price = new Price();

	    String dateAsString = tokens[0];
	    try {
		price.setDate(dateFormat.parse(dateAsString));
	    } catch (ParseException ex) {
		throw new IllegalArgumentException(msg(
			"failed to parse date [{0}]", dateAsString), ex);
	    }

	    String valueAsString = tokens[6];
	    try {
		price.setValue(Double.valueOf(valueAsString));
	    } catch (NumberFormatException ex) {
		throw new IllegalArgumentException(msg(
			"failed to parse double [{0}]", valueAsString), ex);
	    }

	    prices.add(price);

	}

	Collections.sort(prices);

	logger.debug(msg(
		"load [{0}] prices from=[{1,date,yyyy-MM-dd}] to=[{2,date,yyyy-MM-dd}] for stock=[{3}]",
		prices.size(), fromDate, toDate, stock));

	return prices;

    }

}
