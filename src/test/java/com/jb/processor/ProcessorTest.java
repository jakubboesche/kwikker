package com.jb.processor;

import com.jb.processor.MissingTypeException;
import com.jb.processor.Order;
import com.jb.processor.Processor;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class ProcessorTest {
    private static final int VALUE = 1;
    private Processor processor = new Processor();

    @Test
    public void shouldFetchEmptyOrderList() {
        UUID uuid = UUID.randomUUID();
        assertThat(processor.fetch(uuid)).isEmpty();
    }

    @Test
    public void shouldPutAnOrder() throws MissingTypeException {
        Order order = getOrder(UUID.randomUUID());
        processor.put(order);
    }

    @Test
    public void shouldThrowAnExceptionWhenNoType() {
        Order order = new Order(null, 0);
        assertThatThrownBy(() -> processor.put(order))
                .isInstanceOf(MissingTypeException.class);
    }

    @Test
    public void shouldPutAnOrderAndFetchIt() throws MissingTypeException {
        UUID type = UUID.randomUUID();
        Order order = getOrder(type);
        processor.put(order);

        assertThat(processor.fetch(type))
                .extracting(Order::getType, Order::getValue)
                .containsExactly(tuple(type, VALUE));
    }

    @Test
    public void shouldPutTwoOrderAndFetchIt() throws MissingTypeException {
        UUID type = UUID.randomUUID();
        processor.put(getOrder(type));
        processor.put(getOrder(type));

        assertThat(processor.fetch(type))
                .extracting(Order::getType, Order::getValue)
                .containsExactly(
                        tuple(type, VALUE),
                        tuple(type, VALUE)
                );
    }

    @Test
    public void shouldFetchAnOrderForSpecificType() throws MissingTypeException {
        String uuidString = UUID.randomUUID().toString();
        UUID type1 = UUID.fromString(uuidString);
        UUID type2 = UUID.randomUUID();
        processor.put(getOrder(type1));
        processor.put(getOrder(type2));

        assertThat(processor.fetch(UUID.fromString(uuidString)/*type1*/))
                .extracting(Order::getType, Order::getValue)
                .containsExactly(
                        tuple(type1, VALUE)
                );
    }

    @Test
    public void shouldRemoveOrdersAfterFetching() throws MissingTypeException {
        UUID type = UUID.randomUUID();
        Order order = getOrder(type);
        processor.put(order);

        processor.fetch(type);
        assertThat(processor.fetch(type))
                .isEmpty();
    }

    @Test
    public void shouldRemoveOrdersAfterFetchingFromSpecificType() throws MissingTypeException {
        UUID type1 = UUID.randomUUID();
        UUID type2 = UUID.randomUUID();
        processor.put(getOrder(type1));
        processor.put(getOrder(type2));

        processor.fetch(type2);

        assertThat(processor.fetch(type1))
                .extracting(Order::getType, Order::getValue)
                .containsExactly(
                        tuple(type1, VALUE)
                );
        assertThat(processor.fetch(type2))
                .isEmpty();
    }

    private Order getOrder(UUID type) {
        return new Order(type, VALUE);
    }
}