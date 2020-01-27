package com.portfolio.vendingmachine.service;

import com.portfolio.vendingmachine.model.Coin;
import com.portfolio.vendingmachine.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class CoinService {
    @Autowired
    private CoinRepository coinRepository;

    public Set<Coin> getAllCoins() {
        Set<Coin> coins = new TreeSet<>();
        coinRepository.findAll().iterator().forEachRemaining(coins::add);
        return coins;
    }

    public Coin findById(int id) {
        return coinRepository.findById(id).get();
    }

    public Coin findByValue(Integer value) {
        return coinRepository.findByValue(value);
    }

    public void save(Coin coin) {
        coinRepository.save(coin);
    }

    public void updateStock(Coin coin, Integer newStock) {
        coin.setStock(newStock);
        save(coin);
    }

    public void delete(Coin coin) {
        coinRepository.delete(coin);
    }
}
