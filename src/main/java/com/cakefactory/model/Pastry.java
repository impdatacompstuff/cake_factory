package com.cakefactory.model;

import javax.validation.constraints.*;

import javax.money.MonetaryAmount;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;

public class Pastry {

    private String title;
    private Category category;

    // in pounds
    private MonetaryAmount price;

    private String imagePath;

    private String description;

    @Min(value = 0)
    @Max(value = 5)
    private int stars;

    public Pastry(String title, Category category, MonetaryAmount price, String imagePath, String description, int stars) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath;
        this.description = description;
        this.stars = stars;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public String getPrice() {
        return Currency.getInstance(price.getCurrency().getCurrencyCode()).getSymbol() + price.getNumber().toString();
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public double getStars() {
        return stars;
    }
}
