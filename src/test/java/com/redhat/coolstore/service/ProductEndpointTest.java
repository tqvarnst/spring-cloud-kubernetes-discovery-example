package com.redhat.coolstore.service;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.json;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import io.specto.hoverfly.junit.rule.HoverflyRule;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.redhat.coolstore.model.Inventory;
import com.redhat.coolstore.model.Product;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Inventory mockFedoraInventory, mockStickersInventory, mockDefaultInventory;

    static {
        mockFedoraInventory = new Inventory();
        mockFedoraInventory.quantity = 123;
        mockFedoraInventory.itemId = "329299";
        
        mockStickersInventory = new Inventory();
        mockStickersInventory.quantity = 98;
        mockStickersInventory.itemId = "329199";
    
        mockDefaultInventory = new Inventory();
        mockDefaultInventory.quantity = 0;
        mockDefaultInventory.itemId = "Undefined";
    }

    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
            service("mock.service.url:9999")
                    .get(startsWith("/services/inventory/329299"))
                        .willReturn(success(json(mockFedoraInventory)))
                    .get(startsWith("/services/inventory/329199"))
                        .willReturn(success(json(mockStickersInventory)))
                    .get(startsWith("/services/inventory"))
                        .willReturn(success(json(mockDefaultInventory)))
    ));
    
    @Test
    public void test_retriving_one_proudct() {
        ResponseEntity<Product> response
                = restTemplate.getForEntity("/services/product/329199", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product product = response.getBody();
        assertThat(product)
                .returns("329199",p -> p.itemId)
                .returns("Forge Laptop Sticker",p -> p.name)
                .returns(98,p -> p.quantity)
                .returns(8.50,p -> p.price);
    }
    
    @Test
    public void check_that_endpoint_returns_a_correct_list() {
        ResponseEntity<List<Product>> rateResponse =
                restTemplate.exchange("/services/products",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
                        });

        List<Product> productList = rateResponse.getBody();
        assertThat(productList).isNotNull();
        assertThat(productList).isNotEmpty();
        List<String> names = productList.stream().map(p -> p.name).collect(Collectors.toList());
        assertThat(names).contains("Red Fedora","Forge Laptop Sticker","Oculus Rift");   
    }   
}
