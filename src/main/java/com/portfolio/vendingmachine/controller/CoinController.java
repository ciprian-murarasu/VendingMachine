package com.portfolio.vendingmachine.controller;

import com.portfolio.vendingmachine.model.Coin;
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
import java.util.Set;

@Controller
public class CoinController {
    private CoinService coinService;
    private ProductService productService;

    @Autowired
    public CoinController(CoinService coinService, ProductService productService) {
        this.coinService = coinService;
        this.productService = productService;
    }

    @GetMapping("/add_coin")
    public String showAddForm(@ModelAttribute("coin") Coin coin) {
        return "add_coin";
    }

    @PostMapping("/add_coin")
    public String addCoin(@Valid Coin coin, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add_coin";
        }
        Set<Coin> coins = coinService.getAllCoins();
        for (Coin c : coins) {
            if ((int) c.getValue() == coin.getValue()) {
                model.addAttribute("errorMessage", "This coin type already exists!");
                return "add_coin";
            }
        }
        coinService.save(coin);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("coins", coinService.getAllCoins());
        return "redirect:/maintenance";
    }

//    @GetMapping("/edit_coin/{id}")
//    public String showUpdateForm(@PathVariable("id") int id, Model model) {
//        Coin coin = coinService.findById(id);
//        model.addAttribute("coin", coin);
//        return "update_coin";
//    }

    @PostMapping("/update_coin/{id}")
    public String updateCoin(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        String reqParam = request.getParameter("new_stock_for_coin_id_" + id);
        if (reqParam.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid value!");
        } else {
            int newStock = Integer.parseInt(reqParam);
            Coin coinToUpdate = coinService.findById(id);
            coinToUpdate.setStock(newStock);
            coinService.save(coinToUpdate);
        }
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("coins", coinService.getAllCoins());
        return "redirect:/maintenance";
    }

    @GetMapping("/delete_coin/{id}")
    public String deleteCoin(@PathVariable("id") int id, Model model) {
        Coin coin = coinService.findById(id);
        coinService.delete(coin);
        model.addAttribute("coin", coin);
        return "redirect:/maintenance";
    }
}
