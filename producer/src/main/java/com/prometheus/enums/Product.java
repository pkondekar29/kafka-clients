package com.prometheus.enums;

import java.util.Random;

public enum Product {

    PENCIL("Pencil"),
    PEN("Pen"),
    BOOK("Book"),
    MOBILE("Mobile"),
    LAPTOP("Laptop"),
    TV("TV"),
    SHIRT("Shirt"),
    BOTTLE("Bottle"),
    HARD_DISK("Hard Disk"),
    SHARPENER("Sharpener");

    String productName;

    Product(String productName) {
        this.productName = productName;
    }

    public static Product getRandom() {
        Random random = new Random();
        Product[] products = Product.values();
        return products[random.nextInt(products.length)];
    }

    @Override
    public String toString() {
        return productName;
    }
}
