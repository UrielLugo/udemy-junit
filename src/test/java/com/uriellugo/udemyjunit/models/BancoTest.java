package com.uriellugo.udemyjunit.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {

    @Test
    void test_reflectFields() {
        Field[] declaredFields = Banco.class.getDeclaredFields();
        List<String> fieldsList = Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertNotEquals(0, fieldsList.size());
        assertEquals(4, fieldsList.size());
        fieldsList.forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(classes = {Column.class, Id.class, GeneratedValue.class})
    void test_getAnnotationsFields(Class<? extends Annotation> annotation) {
        Field[] declaredFields = Banco.class.getDeclaredFields();
        List<Field> fieldsWithAnnotation = Arrays.stream(declaredFields)
                .filter(f -> f.isAnnotationPresent(annotation))
                .collect(Collectors.toList());

        assertNotEquals(0, fieldsWithAnnotation.size());

        fieldsWithAnnotation.forEach(f -> {
                    System.out.println("Field: " + f.getName());
                    Annotation annotation1 = f.getAnnotation(annotation);
                    System.out.println("Annotations: " + annotation1);
                    System.out.println("Annotation Type: " + annotation1.annotationType());
                });
    }

    @Test
    void test_classConstructors() {
        try {
            Class<?> aClass = Class.forName("com.uriellugo.udemyjunit.models.Banco");
            System.out.println("aClass.getName() = " + aClass.getName());
            System.out.println("aClass.getSimpleName() = " + aClass.getSimpleName());
            System.out.println("aClass.getTypeName() = " + aClass.getTypeName());
            Banco object = (Banco) aClass.getConstructor(Long.class, String.class, java.util.List.class, int.class)
                    .newInstance(8L, "El banco de las luces", Collections.singletonList(new Cuenta(1L, "Uriel Lugo", new BigDecimal(6000))), 10000);
            System.out.println("object = " + object + "\n");

            Constructor<?>[] constructors = aClass.getDeclaredConstructors();
            System.out.println("Constructores declarados en la clase " + aClass.getSimpleName());
            Arrays.stream(constructors).forEach(System.out::println);

            System.out.println("\nConsiguiendo una instancia con el constructor privado...");
            Constructor<Banco> privateConstructor = Banco.class.getDeclaredConstructor(String[].class);
            privateConstructor.setAccessible(true);
            String[] strings = {"Hola", "Mundo", "desde México", "a las " + LocalTime.now()};
            Banco banco = privateConstructor.newInstance(new Object[]{strings});
            assertNotNull(banco);
            System.out.println("banco = " + banco);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}