package com.portfolio.vendingmachine.controller;

import com.portfolio.vendingmachine.model.Product;
import com.portfolio.vendingmachine.service.CoinService;
import com.portfolio.vendingmachine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ProductController {
    private ProductService productService;
    private CoinService coinService;

    @Autowired
    public ProductController(ProductService productService, CoinService coinService) {
        this.productService = productService;
        this.coinService = coinService;
    }

    @GetMapping("/add_product")
    public String showAddForm(@ModelAttribute("product") Product product) {
        return "add_product";
    }

    @PostMapping("/add_product")
    public String addProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add_product";
        }
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            if (p.getName().toLowerCase().equals(product.getName().toLowerCase()) &&
                    p.getUnitQty().toLowerCase().equals(product.getUnitQty().toLowerCase())) {
                model.addAttribute("errorMessage", "This product already exists!");
                return "add_product";
            }
        }
        productService.save(product);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("coins", coinService.getAllCoins());
        return "redirect:/maintenance";
    }

//    @GetMapping("/edit_product/{id}")
//    public String showUpdateForm(@PathVariable("id") int id, Model model) {
//        Product product = productService.findById(id);
//        model.addAttribute("product", product);
//        return "update_product";
//    }

    @PostMapping("/update_product/{id}")
    public String updateProduct(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        String reqParam1 = request.getParameter("new_price_for_product_id_" + id);
        String reqParam2 = request.getParameter("new_stock_for_product_id_" + id);
        if (reqParam1.isEmpty() || reqParam2.isEmpty()) {
            model.addAttribute("errorMessage", "Some of the values are invalid!");
        } else {
            int newPrice = Integer.parseInt(reqParam1);
            int newStock = Integer.parseInt(reqParam2);
            Product productToUpdate = productService.findById(id);
            productToUpdate.setPrice(newPrice);
            productToUpdate.setStock(newStock);
            productService.save(productToUpdate);
        }
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("coins", coinService.getAllCoins());
        return "redirect:/maintenance";
    }

    @GetMapping("/delete_product/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        productService.delete(product);
        model.addAttribute("product", product);
        return "redirect:/maintenance";
    }
}
