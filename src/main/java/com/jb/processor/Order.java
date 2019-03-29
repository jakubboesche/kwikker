package com.jb.processor;

import java.util.UUID;

public class Order {
    private final UUID type;
    private final int value;

    public Order(UUID type, int value) {
        this.type = type;
        this.value = value;
    }

    public UUID getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
