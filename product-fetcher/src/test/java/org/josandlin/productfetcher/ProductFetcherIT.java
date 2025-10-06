package org.josandlin.productfetcher;

import com.github.tomakehurst.wiremock.WireMockServer;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductFetcherIT {

    @LocalServerPort
    private Integer port;

    static WireMockServer wireMockServer;

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
        registry.add("product.fetcher.url", () -> "http://localhost:" + wireMockServer.port() + "/products");
    }

    @BeforeAll
    static void beforeAll() {
        mysql.start();
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        stubFor(get(urlEqualTo("/products"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getMockProductsJson())));
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
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
                .get(targetUrl)
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


    private static String getMockProductsJson() {
        return """
                [
                    {
                        "id": 1,
                        "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                        "price": 109.95,
                        "description": "Your perfect pack for everyday use and walks in the forest.",
                        "category": "men's clothing",
                        "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
                        "rating": {
                            "rate": 3.9,
                            "count": 120
                        }
                    },
                    {
                        "id": 2,
                        "title": "Mens Casual Premium Slim Fit T-Shirts",
                        "price": 22.3,
                        "description": "Slim-fitting style, contrast raglan long sleeve.",
                        "category": "men's clothing",
                        "image": "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg",
                        "rating": {
                            "rate": 4.1,
                            "count": 259
                        }
                    },
                    {
                        "id": 3,
                        "title": "Mens Cotton Jacket",
                        "price": 55.99,
                        "description": "Great outerwear jackets for Spring/Autumn/Winter.",
                        "category": "men's clothing",
                        "image": "https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg",
                        "rating": {
                            "rate": 4.7,
                            "count": 500
                        }
                    },
                    {
                        "id": 4,
                        "title": "Mens Casual Slim Fit",
                        "price": 15.99,
                        "description": "The color could be slightly different between on the screen and in practice.",
                        "category": "men's clothing",
                        "image": "https://fakestoreapi.com/img/71YXzeOuslL._AC_UY879_.jpg",
                        "rating": {
                            "rate": 2.1,
                            "count": 430
                        }
                    },
                    {
                        "id": 5,
                        "title": "John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet",
                        "price": 695,
                        "description": "From our Legends Collection, the Naga was inspired by the mythical water dragon.",
                        "category": "jewelery",
                        "image": "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
                        "rating": {
                            "rate": 4.6,
                            "count": 400
                        }
                    },
                    {
                        "id": 6,
                        "title": "Solid Gold Petite Micropave",
                        "price": 168,
                        "description": "Satisfaction Guaranteed. Return or exchange any order within 30 days.",
                        "category": "jewelery",
                        "image": "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_.jpg",
                        "rating": {
                            "rate": 3.9,
                            "count": 70
                        }
                    },
                    {
                        "id": 7,
                        "title": "White Gold Plated Princess",
                        "price": 9.99,
                        "description": "Classic Created Wedding Engagement Solitaire Diamond Promise Ring.",
                        "category": "jewelery",
                        "image": "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg",
                        "rating": {
                            "rate": 3,
                            "count": 400
                        }
                    },
                    {
                        "id": 8,
                        "title": "Pierced Owl Rose Gold Plated Stainless Steel Double",
                        "price": 10.99,
                        "description": "Rose Gold Plated Double Flared Tunnel Plug Earrings.",
                        "category": "jewelery",
                        "image": "https://fakestoreapi.com/img/51UDEzMJVpL._AC_UL640_QL65_ML3_.jpg",
                        "rating": {
                            "rate": 1.9,
                            "count": 100
                        }
                    },
                    {
                        "id": 9,
                        "title": "WD 2TB Elements Portable External Hard Drive - USB 3.0",
                        "price": 64,
                        "description": "USB 3.0 and USB 2.0 Compatibility Fast data transfers.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg",
                        "rating": {
                            "rate": 3.3,
                            "count": 203
                        }
                    },
                    {
                        "id": 10,
                        "title": "SanDisk SSD PLUS 1TB Internal SSD - SATA III 6 Gb/s",
                        "price": 109,
                        "description": "Easy upgrade for faster boot up, shutdown, application load.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/61U7T1koQqL._AC_SX679_.jpg",
                        "rating": {
                            "rate": 2.9,
                            "count": 470
                        }
                    },
                    {
                        "id": 11,
                        "title": "Silicon Power 256GB SSD 3D NAND A55 SLC Cache Performance Boost",
                        "price": 109,
                        "description": "3D NAND flash are applied to deliver high transfer speeds.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/71kWymZ+c+L._AC_SX679_.jpg",
                        "rating": {
                            "rate": 4.8,
                            "count": 319
                        }
                    },
                    {
                        "id": 12,
                        "title": "WD 4TB Gaming Drive Works with Playstation 4 Portable External Hard Drive",
                        "price": 114,
                        "description": "Expand your PS4 gaming experience, Play anywhere.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/61mtL65D4cL._AC_SX679_.jpg",
                        "rating": {
                            "rate": 4.8,
                            "count": 400
                        }
                    },
                    {
                        "id": 13,
                        "title": "Acer SB220Q bi 21.5 inches Full HD (1920 x 1080) IPS Ultra-Thin",
                        "price": 599,
                        "description": "21.5 inches Full HD (1920 x 1080) widescreen IPS display.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/81QpkIctqPL._AC_SX679_.jpg",
                        "rating": {
                            "rate": 2.9,
                            "count": 250
                        }
                    },
                    {
                        "id": 14,
                        "title": "Samsung 49-Inch CHG90 144Hz Curved Gaming Monitor",
                        "price": 999.99,
                        "description": "49 INCH SUPER ULTRAWIDE 32:9 CURVED GAMING MONITOR.",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/81Zt42ioCgL._AC_SX679_.jpg",
                        "rating": {
                            "rate": 2.2,
                            "count": 140
                        }
                    },
                    {
                        "id": 15,
                        "title": "BIYLACLESEN Women's 3-in-1 Snowboard Jacket Winter Coats",
                        "price": 56.99,
                        "description": "Note:The Jackets is US standard size.",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/51Y5NI-I5jL._AC_UX679_.jpg",
                        "rating": {
                            "rate": 2.6,
                            "count": 235
                        }
                    },
                    {
                        "id": 16,
                        "title": "Lock and Love Women's Removable Hooded Faux Leather Moto Biker Jacket",
                        "price": 29.95,
                        "description": "100% POLYURETHANE(shell) 100% POLYESTER(lining).",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/81XH0e8fefL._AC_UY879_.jpg",
                        "rating": {
                            "rate": 2.9,
                            "count": 340
                        }
                    },
                    {
                        "id": 17,
                        "title": "Rain Jacket Women Windbreaker Striped Climbing Raincoats",
                        "price": 39.99,
                        "description": "Lightweight perfet for trip or casual wear.",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/71HblAHs5xL._AC_UY879_-2.jpg",
                        "rating": {
                            "rate": 3.8,
                            "count": 679
                        }
                    },
                    {
                        "id": 18,
                        "title": "MBJ Women's Solid Short Sleeve Boat Neck V",
                        "price": 9.85,
                        "description": "95% RAYON 5% SPANDEX, Made in USA or Imported.",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/71z3kpMAYsL._AC_UY879_.jpg",
                        "rating": {
                            "rate": 4.7,
                            "count": 130
                        }
                    },
                    {
                        "id": 19,
                        "title": "Opna Women's Short Sleeve Moisture",
                        "price": 7.95,
                        "description": "100% Polyester, Machine wash, 100% cationic polyester interlock.",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/51eg55uWmdL._AC_UX679_.jpg",
                        "rating": {
                            "rate": 4.5,
                            "count": 146
                        }
                    },
                    {
                        "id": 20,
                        "title": "DANVOUY Womens T Shirt Casual Cotton Short",
                        "price": 12.99,
                        "description": "95%Cotton,5%Spandex, Features: Casual, Short Sleeve.",
                        "category": "women's clothing",
                        "image": "https://fakestoreapi.com/img/61pHAEJ4NML._AC_UX679_.jpg",
                        "rating": {
                            "rate": 3.6,
                            "count": 145
                        }
                    }
                ]
                """;
    }


}
