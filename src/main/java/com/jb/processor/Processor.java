package com.jb.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class Processor {
    private final Map<UUID, Collection<Order>> ordersByType = new ConcurrentHashMap<>();

    public Collection<Order> fetch(UUID uuid) {
        return ofNullable(ordersByType.remove(uuid)).orElse(emptyList());
    }

    public void put(Order order) throws MissingTypeException {
        UUID type = order.getType();
        if (type == null) {
            throw new MissingTypeException();
        }
        ordersByType.compute(type, (uuid, orders) -> withOrder(orders, order));
    }

    private Collection<Order> withOrder(Collection<Order> orders, Order order) {
        Collection<Order> newOrders = ofNullable(orders).orElse(new ArrayList<>());
        newOrders.add(order);
        return newOrders;
    }
}
