package com.zakmouf.pms.dao;

import java.util.List;

import com.zakmouf.pms.domain.Portfolio;

public interface PortfolioDao {

    List<Portfolio> findAll();

    Portfolio findById(Long id);

    void insert(Portfolio portfolio);

    void update(Portfolio portfolio);

    void delete(Portfolio portfolio);

}
