package com.redhat.coolstore.service;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import com.redhat.coolstore.model.Product;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @Test
    public void test_readOne() {
        Product product = repository.findById("444434");
        assertThat(product).isNotNull();
        assertThat(product.name).as("Verify product name").isEqualTo("Pebble Smart Watch");
        assertThat(product.price).as("Price should match ").isEqualTo(24.0);
    }

    @Test
    public void test_readAll() {
        List<Product> productList = repository.readAll();
        assertThat(productList).isNotNull();
        assertThat(productList).isNotEmpty();
        List<String> names = productList.stream().map(p -> p.name).collect(Collectors.toList());
        assertThat(names).contains("Red Fedora","Forge Laptop Sticker","Oculus Rift","Solid Performance Polo","16 oz. Vortex Tumbler","Pebble Smart Watch","Lytro Camera");
    }

}