//package org.josandlin.webapp;
//
//import io.restassured.RestAssured;
//
//
//import org.josandlin.webapp.FakeStoreProductFetcher;
//import org.josandlin.webapp.dao.OrderDao;
//import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
//import org.josandlin.nacbackend2groupjosandlin.dao.RoleDao;
//import org.josandlin.nacbackend2groupjosandlin.dao.UserDao;
//import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
//import org.josandlin.nacbackend2groupjosandlin.dto.RatingDTO;
//import org.josandlin.nacbackend2groupjosandlin.service.ProductService;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class IntegrationTests {
//
//    @LocalServerPort
//    private Integer port;
//
//
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.4")
//
//            .withDatabaseName("backend_db")
//            .withUsername("backend_app")
//            .withPassword("test1234")
//            .withInitScript("db/init.sql");
//
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> mysql.getJdbcUrl());
//        registry.add("spring.datasource.username", () -> mysql.getUsername());
//        registry.add("spring.datasource.password", () -> mysql.getPassword());
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        mysql.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        mysql.stop();
//    }
//
//    @Autowired
//    OrderDao orderDao;
//
//    @Autowired
//    ProductDao productDao;
//
//    @Autowired
//    RoleDao roleDao;
//
//    @Autowired
//    UserDao userDao;
//
//    @Autowired
//    ProductService productService;
//
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.baseURI = "http://localhost:" + port;
////        orderDao.deleteAll();
////        productDao.deleteAll();
////        roleDao.deleteAll();
////        userDao.deleteAll();
//    }
//
//
//    @Test
//    void getAllProductsFromFakeStoreShouldReturnAllProducts() {
//
//        try {
//            List<ProductDTO> products = FakeStoreProductFetcher.fetchProducts();
//
//            assertEquals(products.stream().map(ProductDTO::getId).filter(id -> id > 0).toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getCategory).filter(category -> category != null &&
//                                    !category.isEmpty())
//                            .toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getDescription).filter(description -> description != null &&
//                                    !description.isEmpty())
//                            .toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getTitle).filter(title -> title != null &&
//                                    !title.isEmpty())
//                            .toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getImage).filter(imageURL -> imageURL != null &&
//                                    !imageURL.isEmpty())
//                            .toList().size()
//                    , products.size());
//
//
//            assertEquals(products.stream().map(ProductDTO::getPrice).filter(price -> price > -1)
//                            .toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getCount).filter(count -> count > -1)
//                            .toList().size()
//                    , products.size());
//
//            assertEquals(products.stream().map(ProductDTO::getRating).map(RatingDTO::getRate).filter(count -> count > -1)
//                            .toList().size()
//                    , products.size());
//
//        } catch (Exception e) {
//            assertFalse(e instanceof Exception);
//        }
//
//    }
//
//    @Test
//    void SaveAllProductsFromFakeStoreShouldReturnSameAmountOfProducts() {
//
//        try {
//            List<ProductDTO> productDTOs = FakeStoreProductFetcher.fetchProducts();
//            productService.saveAll(productDTOs);
//
//            assertEquals(productService.getProducts().size(), productDTOs.size());
//
//        } catch (Exception e) {
//            assertFalse(e instanceof Exception);
//        }
//
//    }
//
//
//}
