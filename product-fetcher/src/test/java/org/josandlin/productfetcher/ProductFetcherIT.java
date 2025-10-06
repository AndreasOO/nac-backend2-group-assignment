package org.josandlin.productfetcher;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.josandlin.library.entity.product.Product;
import org.josandlin.library.fetcher.Fetcher;
import org.josandlin.productfetcher.service.ProductService;
import org.josandlin.productfetcher.dao.ProductDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.dto.RatingDTO;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductFetcherIT {

    @LocalServerPort
    private Integer port;


    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.4")

            .withDatabaseName("product_db")
            .withUsername("product_app")
            .withPassword("test1234")
            .withInitScript("db/init.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
        registry.add("spring.datasource.username", () -> mysql.getUsername());
        registry.add("spring.datasource.password", () -> mysql.getPassword());
    }

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }


    @Autowired
    ProductDao productDao;

    @Autowired
    ProductService productService;

    @Autowired
    Fetcher productFetcher;

    @Value("${product.fetcher.url}")
    private String targetUrl;


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productDao.deleteAll();
    }

    @Test
    void getAllProductsFromFakeStoreShouldReturnCorrectJsonProperties() {
        Response response = given()
                .when()
                .get("https://fakestoreapi.com/products")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract()
                .response();

        String json = response.getBody().asString();

        assertTrue(json.contains("id"));
        assertTrue(json.contains("title"));
        assertTrue(json.contains("price"));
        assertTrue(json.contains("description"));
        assertTrue(json.contains("category"));
        assertTrue(json.contains("image"));
        assertTrue(json.contains("rating"));
        assertTrue(json.contains("rate"));
        assertTrue(json.contains("count"));

    }


    @Test
    void getAllProductsFromFakeStoreShouldReturnAllProducts() {

        try {
            List<ProductDTO> products = productFetcher.fetchProducts(targetUrl);

            assertEquals(products.stream().map(ProductDTO::getId).filter(id -> id > 0).toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getCategory).filter(category -> category != null &&
                                    !category.isEmpty())
                            .toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getDescription).filter(description -> description != null &&
                                    !description.isEmpty())
                            .toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getTitle).filter(title -> title != null &&
                                    !title.isEmpty())
                            .toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getImage).filter(imageURL -> imageURL != null &&
                                    !imageURL.isEmpty())
                            .toList().size()
                    , products.size());


            assertEquals(products.stream().map(ProductDTO::getPrice).filter(price -> price > -1)
                            .toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getCount).filter(count -> count > -1)
                            .toList().size()
                    , products.size());

            assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getRate).filter(count -> count > -1)
                            .toList().size()
                    , products.size());

        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }


    @Test
    void saveAllProductsFromFakeStoreShouldReturnAtLeastOneProduct() {

        try {
            List<ProductDTO> productDTOs = productFetcher.fetchProducts(targetUrl);
            productService.saveAll(productDTOs);

            assertFalse(productService.getProducts().isEmpty());


        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }

    @Test
    void savedProductsAndDTOShouldHaveSameData() {

        try {
            List<ProductDTO> productDTOs = productFetcher.fetchProducts(targetUrl);
            productService.saveAll(productDTOs);

            Long productCount = productDTOs.stream()
                                           .filter(dto -> {
                                                                Product entity = productDao.findById(dto.getId()).orElseThrow();
                                                                return entity.getId().equals(dto.getId()) &&
                                                                       entity.getCategory().equals(dto.getCategory()) &&
                                                                       entity.getDescription().equals(dto.getDescription()) &&
                                                                       entity.getPrice() == dto.getPrice() &&
                                                                       entity.getTitle().equals(dto.getTitle()) &&
                                                                       entity.getImage().equals(dto.getImage()) &&
                                                                       entity.getRating().getCount() == dto.getRating().getCount() &&
                                                                       entity.getRating().getRate() == dto.getRating().getRate();
                                                                    })
                                           .count();


            assertEquals(productCount, productDTOs.size());

        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }

    @Test
    void saveAllProductsFromFakeStoreShouldReturnSameAmountOfProducts() {

        try {
            List<ProductDTO> productDTOs = productFetcher.fetchProducts(targetUrl);
            productService.saveAll(productDTOs);

            assertEquals(productService.getProducts().size(), productDTOs.size());

        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }


}
