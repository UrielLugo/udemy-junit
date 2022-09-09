package com.uriellugo.udemyjunit.repositories;

import com.uriellugo.udemyjunit.models.DatosPersonales;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

class DatosPersonalesRepositoryTest {

    private final List<Object> list = new ArrayList<>();

    @Test
    void test_datosPersonales() {

        list.add("Hello World");
        list.add(2020);
        list.add(true);
        list.add(0.24f);
        list.add("Hola mundo");

        usingConsumer(LocalDateTime.now(), System.out::println, list::add, this::printList);

        byte[] byteArray = {81, 109, 70, 122, 90, 83, 65, 50, 78, 67, 66, 84, 100, 72, 74, 108, 89, 87, 48, 61};
        System.out.println(new String(byteArray, 0, byteArray.length, StandardCharsets.UTF_8));

        DatosPersonales build = DatosPersonales.builder().lista("Doctor").lista("Medico").build();
        DatosPersonales buildGeneric = DatosPersonales.builderGeneric().build();

    }

    public <T> void printList(T t) {
        System.out.println("Se añadio un elemento a la lista: " + t);
        list.stream().filter(((Predicate<? super Object>) c -> c instanceof String))
                .forEach(System.out::println);
    }

    public <T> void usingConsumer(T value, Consumer<T> consume, Consumer<T> consumer2, Consumer<T> consumer3 ) {
        Consumer<T> finalConsumer = consume.andThen(consumer2).andThen(consumer3);
        finalConsumer.accept(value);
    }

}