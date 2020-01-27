package com.portfolio.vendingmachine.repository;

import com.portfolio.vendingmachine.model.Coin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends CrudRepository<Coin, Integer> {
    Coin findByValue(Integer value);
}
