package com.portfolio.vendingmachine.service;

import com.portfolio.vendingmachine.model.Product;
import com.portfolio.vendingmachine.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().iterator().forEachRemaining(products::add);
        return products;
    }

    public Product findById(int id) {
        return productRepository.findById(id).get();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void updateStock(Product product, Integer newStock) {
        product.setStock(newStock);
        save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
