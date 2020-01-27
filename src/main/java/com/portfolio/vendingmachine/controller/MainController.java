package com.portfolio.vendingmachine.controller;

import com.portfolio.vendingmachine.model.Coin;
import com.portfolio.vendingmachine.model.Product;
import com.portfolio.vendingmachine.service.CoinService;
import com.portfolio.vendingmachine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
public class MainController {
    private final CoinService coinService;
    private final ProductService productService;

    @Autowired
    public MainController(CoinService coinService, ProductService productService) {
        this.coinService = coinService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String goToIndex(Model model) {
        model.addAttribute("coins", coinService.getAllCoins());
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/maintenance")
    public String goToMaintenance(Model model) {
        model.addAttribute("coins", coinService.getAllCoins());
        model.addAttribute("products", productService.getAllProducts());
        return "maintenance";
    }

    @PostMapping("/")
    public String processOrder(HttpServletRequest request, Model model) {
        int payedAmount = Integer.parseInt(request.getParameter("payed"));
        String productIdParam = request.getParameter("product_id");
        String errorMessage = "";
        if (productIdParam == null) {
            errorMessage = "Unable to process request!";
        } else {
            int productId = Integer.parseInt(productIdParam);
            Product selectedProduct = productService.findById(productId);
            if (selectedProduct.getStock() > 0) {
                int selectedProductPrice = selectedProduct.getPrice();
                if (payedAmount >= selectedProductPrice) {
                    int rest = payedAmount - selectedProductPrice;
                    Set<Coin> coins = coinService.getAllCoins();
                    int[] coinsForRest = new int[coins.size()];
                    boolean canPayRest = true;
                    int i = 0;
                    for (Coin coin : coins) {
                        int currentCoin = coin.getValue();
                        if (coinService.findByValue(currentCoin).getStock() > 0) {
                            coinsForRest[i] = rest / currentCoin;
                            rest -= coinsForRest[i] * currentCoin;
                            if (coinsForRest[i] > coinService.findByValue(currentCoin).getStock()) {
                                canPayRest = false;
                            }
                        }
                        i++;
                    }
                    if (canPayRest) {
                        productService.updateStock(selectedProduct, selectedProduct.getStock() - 1);
                        i = 0;
                        for (Coin coin : coins) {
                            int currentCoinValue = coin.getValue();
                            coinService.updateStock(coinService.findByValue(currentCoinValue), coinService.findByValue(currentCoinValue).getStock() - coinsForRest[i] + Integer.parseInt(request.getParameter("coin_value_" + currentCoinValue)));
                            i++;
                        }
                        model.addAttribute("payedAmount", "0");
                    } else errorMessage = "Unable to pay back rest!";
                } else errorMessage = "Insufficient funds!";
            } else errorMessage = "Out of stock for selected product!";
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("coins", coinService.getAllCoins());
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }
}
