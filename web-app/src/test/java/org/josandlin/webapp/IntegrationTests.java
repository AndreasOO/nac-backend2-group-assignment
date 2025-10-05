package org.josandlin.webapp;

import io.restassured.RestAssured;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.josandlin.library.entity.product.Product;
import org.josandlin.library.fetcher.Fetcher;
import org.josandlin.webapp.dao.OrderDao;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dao.RoleDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.dto.RatingDTO;
import org.josandlin.webapp.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests {

    @LocalServerPort
    private Integer port;

    static Network network = Network.newNetwork();

    static MySQLContainer<?> productFetcherDb = new MySQLContainer<>("mysql:8.4.4")
            .withNetwork(network)
            .withNetworkAliases("pf-db")
            .withDatabaseName("product_db")
            .withUsername("product_app")
            .withPassword("test1234")
            .withInitScript("db/pf-db-init.sql");


    static GenericContainer<?> productFetcherMicroservice = new GenericContainer<>(
            new ImageFromDockerfile()
                    .withDockerfile(Path.of("../Dockerfile.pf")))
            .withNetwork(network)
            .withNetworkAliases("pf")
            .withExposedPorts(8080)
            .withEnv("DB_PASSWORD", "test1234")
            .withEnv("DB_USERNAME", "product_app")
            .withEnv("DB_PORT", "3306")
            .withEnv("DB_HOSTNAME", "pf-db")
            .withEnv("DB_SCHEMA", "product_db")
            .waitingFor(Wait.forHttp("/actuator/health")
                    .forPort(8080)
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(3)))
            .withStartupTimeout(Duration.ofMinutes(3));


    static MySQLContainer<?> webAppDb = new MySQLContainer<>("mysql:8.4.4")
            .withNetwork(network)
            .withNetworkAliases("db")
            .withDatabaseName("backend_db")
            .withUsername("backend_app")
            .withPassword("test1234")
            .withInitScript("db/init.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> webAppDb.getJdbcUrl());
        registry.add("spring.datasource.username", () -> webAppDb.getUsername());
        registry.add("spring.datasource.password", () -> webAppDb.getPassword());
        registry.add("product.fetcher.url", () -> "http://localhost:" + productFetcherMicroservice.getMappedPort(8080) + "/api/products");
    }




    @BeforeAll
    static void beforeAll() {
        productFetcherDb.start();
        webAppDb.start();
        productFetcherMicroservice.start();

        System.out.println("Waiting 15 seconds for products to be fetched and stored");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }

    @AfterAll
    static void afterAll() {
        if (productFetcherMicroservice != null) {
            productFetcherMicroservice.stop();
        }
        productFetcherDb.stop();
        webAppDb.stop();
        network.close();
    }


    @Autowired
    OrderDao orderDao;

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
        orderDao.deleteAll();
//        productDao.deleteAll();
    }

    @Test
    void viewAllProductsFromFakeStoreReturnOk() {
        Response response = given()
                .when()
                .get("http://localhost/8080/products")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    void viewRegisterShouldReturnOk() {
        Response response = given()
                .when()
                .get("http://localhost/8080/register")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    void viewLoginShouldReturnOk() {
        Response response = given()
                .when()
                .get("http://localhost/8080/login")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    void postRegisterUserAsAdminShouldPassAndRedirect() {
        Response response = given()
                .formParam("username", "ITtestuseradmin")
                .formParam("password", "test1234")
                .formParam("role", "ADMIN")
                .when()
                .post("http://localhost/8080/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        String location = response.getHeader("Location");
        assertTrue(location.contains("/loginView"));
    }

    @Test
    void postRegisterUserAsCustomerShouldPassAndRedirect() {
        Response response = given()
                .formParam("username", "ITtestusercustomer")
                .formParam("password", "test1234")
                .formParam("role", "CUSTOMER")
                .when()
                .post("http://localhost/8080/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        String location = response.getHeader("Location");
        assertTrue(location.contains("/loginView"));
    }

    @Test
    void loginAsCustomerShouldPassAndRedirect() {

        Response response1 = given()
                .formParam("username", "ITtestusercustomer")
                .formParam("password", "test1234")
                .formParam("role", "CUSTOMER")
                .when()
                .post("http://localhost/8080/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestusercustomer")
                .formParam("password", "test1234")
                .redirects().follow(false)  // Don't auto-follow redirects
                .when()
                .post("/login")
                .then()
                .statusCode(302)  // Redirect on success
                .extract()
                .response();


        String location = response2.getHeader("Location");
        assertTrue(location.contains("/loginView"));
    }






    @Test
    void getAllProductsFromFakeStoreShouldReturnAllProducts() {

        try {
            List<ProductDTO> products = productFetcher.fetchProducts(targetUrl);

            Assertions.assertEquals(products.stream().map(ProductDTO::getId).filter(id -> id > 0).toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getCategory).filter(category -> category != null &&
                                    !category.isEmpty())
                            .toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getDescription).filter(description -> description != null &&
                                    !description.isEmpty())
                            .toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getTitle).filter(title -> title != null &&
                                    !title.isEmpty())
                            .toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getImage).filter(imageURL -> imageURL != null &&
                                    !imageURL.isEmpty())
                            .toList().size()
                    , products.size());


            Assertions.assertEquals(products.stream().map(ProductDTO::getPrice).filter(price -> price > -1)
                            .toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getCount).filter(count -> count > -1)
                            .toList().size()
                    , products.size());

            Assertions.assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getRate).filter(count -> count > -1)
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


            Assertions.assertEquals(productCount, productDTOs.size());

        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }

    @Test
    void saveAllProductsFromFakeStoreShouldReturnSameAmountOfProducts() {

        try {
            List<ProductDTO> productDTOs = productFetcher.fetchProducts(targetUrl);
            productService.saveAll(productDTOs);

            Assertions.assertEquals(productService.getProducts().size(), productDTOs.size());

        } catch (Exception e) {
            assertFalse(e instanceof Exception);
        }

    }




}
