package org.josandlin.webapp;

import io.restassured.RestAssured;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.josandlin.library.dto.*;
import org.josandlin.library.entity.product.Product;
import org.josandlin.library.entity.product.Rating;
import org.josandlin.library.entity.user.User;
import org.josandlin.library.entity.order.Order;
import org.josandlin.library.fetcher.Fetcher;
import org.josandlin.library.mapper.order.OrderMapper;
import org.josandlin.webapp.dao.OrderDao;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.webapp.service.OrderService;
import org.josandlin.webapp.service.ProductService;
import org.josandlin.webapp.service.UserService;
import org.josandlin.webapp.utils.ResultMessage;
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
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebAppIT {

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
    @Autowired
    private OrderMapper orderMapper;


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
    UserService userService;

    @Autowired
    Fetcher productFetcher;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserDao userDao;

    @Value("${product.fetcher.url}")
    private String targetUrl;


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        orderDao.deleteAll();
//        productDao.deleteAll();
//        userDao.deleteAll();
    }

    @Test
    void createUserTest(){

        int userCountBefore = userService.getAllUsers().size();

        ResultMessage resultMessage;

        resultMessage = userService.createUser(
                new UserCreateDTO("Anna Karenina", "iHeartVronskij", "CUSTOMER"));

        assertEquals(true, resultMessage.isSuccess());
        assertFalse(resultMessage.getMessage().isEmpty());

        int userCountAfter = userService.getAllUsers().size();

        assertTrue(userCountBefore < userCountAfter);
        assertNotEquals(userCountAfter, userCountBefore);
    }

    @Test
    void viewAllProductsFromMicroServiceShouldReturnOk() {
        Response response = given()
                .when()
                .get("/products")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(200)
                .extract()
                .response();
    }


    @Test
    void viewSpecificProductsFromMicroServiceShouldReturnOk() {
        Response response = given()
                .when()
                .get("/products/1")
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
                .get("/register")
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
                .get("/loginView")
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
                .post("/registerUser")
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
                .post("/registerUser")
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
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestusercustomer")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();


        String location = response2.getHeader("Location");
        assertTrue(location.contains("/loginView"));
    }

    @Test
    void loginAsAdminShouldPassAndRedirect() {

        Response response1 = given()
                .formParam("username", "ITtestuseradmin")
                .formParam("password", "test1234")
                .formParam("role", "ADMIN")
                .when()
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestuseradmin")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();


        String location = response2.getHeader("Location");
        assertTrue(location.contains("/loginView"));
    }


    @Test
    void viewOrdersAsAdminShouldShowOrdersView() {

        Response response1 = given()
                .formParam("username", "ITtestuseradmin15")
                .formParam("password", "test1234")
                .formParam("role", "ADMIN")
                .when()
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestuseradmin15")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();

        String sessionId = response2.getCookie("JSESSIONID");

        Response response3 = given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertTrue(response3.getBody().prettyPrint().contains("Order Management - Admin Panel"));
    }



    @Test
    void buyProductAsCustomerShouldCreateOrder() {

        Response response1 = given()
                .formParam("username", "ITtestusercustomer3")
                .formParam("password", "test1234")
                .formParam("role", "CUSTOMER")
                .when()
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestusercustomer3")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();

        long customerOrders = orderDao.findAll().stream()
                                                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer3"))
                                                .count();

        Assertions.assertEquals(0, customerOrders);
        String sessionId = response2.getCookie("JSESSIONID");

        Response response3 = given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .post("/products/1/buy")
                .then()
                .statusCode(302)
                .extract()
                .response();

        long customerOrdersAfterPost = orderDao.findAll().stream()
                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer3"))
                .count();


        Assertions.assertEquals(1, customerOrdersAfterPost);

        String location = response3.getHeader("Location");
        assertTrue(location.contains("/products/1"));
    }



    @Test
    void removeOrderShouldRemoveOrderFromDatabase() {

        Response response1 = given()
                .formParam("username", "ITtestusercustomer5")
                .formParam("password", "test1234")
                .formParam("role", "CUSTOMER")
                .when()
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response2 = given()
                .formParam("username", "ITtestusercustomer5")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();

        long customerOrders = orderDao.findAll().stream()
                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer5"))
                .count();

        Assertions.assertEquals(0, customerOrders);

        String sessionIdForCustomer = response2.getCookie("JSESSIONID");

        Response response3 = given()
                .cookie("JSESSIONID", sessionIdForCustomer)
                .when()
                .post("/products/1/buy")
                .then()
                .statusCode(302)
                .extract()
                .response();

        long customerOrdersAfterPost = orderDao.findAll().stream()
                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer5"))
                .count();

        Assertions.assertEquals(1, customerOrdersAfterPost);



        Long savedOrderId = orderDao.findAll().stream()
                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer5"))
                .findFirst().orElseThrow().getId();

                Response response4 = given()
                .formParam("username", "ITtestuseradmin4")
                .formParam("password", "test1234")
                .formParam("role", "ADMIN")
                .when()
                .post("/registerUser")
                .then()
                .statusCode(302)
                .extract()
                .response();

        Response response5 = given()
                .formParam("username", "ITtestuseradmin4")
                .formParam("password", "test1234")
                .redirects().follow(false)
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .extract()
                .response();

        String sessionId = response5.getCookie("JSESSIONID");


        Response response6 = given()
                .cookie("JSESSIONID", sessionId)
                .when()
                .post("/orders/"+savedOrderId+"/delete")
                .then()
                .statusCode(302)
                .extract()
                .response();

        String location = response6.getHeader("Location");
        assertTrue(location.contains("/orders"));

        long customerOrdersAfterDelete = orderDao.findAll().stream()
                .filter(order -> order.getUser().getUsername().equals("ITtestusercustomer5"))
                .count();

        Assertions.assertEquals(0, customerOrdersAfterDelete);

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



    @Test
    void createOrderServiceShouldReturnResultMessage(){
        productDao.save( new Product( 1L, "title1", 100, "desc1", "catg1", "img1", new Rating(1,1)));

        userDao.save(new User("name", "pw"));
        User savedUser = userDao.findByUsername("name").get();

        ResultMessage result = orderService.createOrder(new OrderCreateDTO(1L, savedUser.getId(), List.of(1L)));

        assertEquals("Order created successfully.", result.getMessage());
        assertTrue(result.isSuccess());

        ResultMessage errorResult = orderService.createOrder(new OrderCreateDTO(1L, 33L, List.of(100L)));

        assertEquals("Purchase failed; no customer found. Please try login in again.", errorResult.getMessage());
        assertFalse(errorResult.isSuccess());

    }

    @Test
    void getOrderByIdServiceShouldReturnId(){
        productDao.save(new Product( 1L, "title1", 100, "desc1", "catg1", "img1", new Rating(1,1)));
        Product savedProduct = productDao.findAll().getFirst();

        userDao.save(new User("test", "pw"));
        User savedUser = userDao.findAll().getFirst();

        orderDao.save(new Order(savedUser, List.of(savedProduct)));
        Order savedOrder = orderDao.findAll().getFirst();

        OrderDTO expectedDTO = new OrderDTO(savedOrder.getId(),
                                            new UserSummaryDTO(savedUser.getId(), savedUser.getUsername()),
                                            List.of(new ProductSummaryDTO(savedProduct.getId(), savedProduct.getTitle(), savedProduct.getPrice())));

        OrderDTO actual = orderService.getOrderById(savedOrder.getId());

        assertEquals(expectedDTO.getId(), actual.getId());

        assertEquals(expectedDTO.getUser().getId(), actual.getUser().getId());
        assertEquals(expectedDTO.getUser().getUsername(), actual.getUser().getUsername());

        assertEquals(expectedDTO.getProducts().getFirst().getId(), actual.getProducts().getFirst().getId());
        assertEquals(expectedDTO.getProducts().getFirst().getTitle(), actual.getProducts().getFirst().getTitle());
    }

}
