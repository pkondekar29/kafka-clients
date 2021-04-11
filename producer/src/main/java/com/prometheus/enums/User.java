package com.prometheus.enums;

import java.util.Random;

public enum User {
    palash("palash","kondekarpalash@gmail.com", "7709193993"),
    hardik("hardik", "kondekarpalash@gmail.com", "7709193993"),
    shanti("shanti", "kondekarpalash@gmail.com", "7709193993"),
    pooja("pooja", "kondekarpalash@gmail.com", "7709193993"),
    kanchan("kanchan", "kkondekar26@gmail.com", "+919096690665");

    private String userName;
    private String email;
    private String mobileNumber;

    User(String userName, String email, String mobileNumber) {
        this.userName = userName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public static User getRandom() {
        Random random = new Random();
        User[] users = User.values();
        return users[random.nextInt(users.length)];
    }
}
