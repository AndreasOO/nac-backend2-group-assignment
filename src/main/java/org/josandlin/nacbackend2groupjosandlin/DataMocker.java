package org.josandlin.nacbackend2groupjosandlin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.nacbackend2groupjosandlin.dao.OrderDao;
import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
import org.josandlin.nacbackend2groupjosandlin.dao.RoleDao;
import org.josandlin.nacbackend2groupjosandlin.dao.UserDao;
import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Order;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;
import org.josandlin.nacbackend2groupjosandlin.entity.Role;
import org.josandlin.nacbackend2groupjosandlin.entity.User;
import org.josandlin.nacbackend2groupjosandlin.mapper.ProductMapper;
import org.josandlin.nacbackend2groupjosandlin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
@org.springframework.core.annotation.Order(2)
public class DataMocker  implements CommandLineRunner{

    OrderDao orderDao;
    RoleDao roleDao;
    UserDao userDao;
    ProductDao productDao;
    ProductService productService;
    ProductMapper productMapper;

    public DataMocker(OrderDao orderDao, RoleDao roleDao, UserDao userDao, ProductDao productDao, ProductService productService, ProductMapper productMapper) {
        this.orderDao = orderDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.productDao = productDao;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public void run(String... args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Role admin = new Role("ADMIN");
        Role customer = new Role("CUSTOMER");
        roleDao.save(admin);
        roleDao.save(customer);

        User linn = new User("Linn", encoder.encode("bulle1234"));
        linn.getRoles().add(admin);
        linn.getRoles().add(customer);
        userDao.save(linn);

        User ylva = new User("Ylva", encoder.encode("test1234"));
        ylva.getRoles().add(admin);
        ylva.getRoles().add(customer);
        userDao.save(ylva);

        User andreas = new User("Andreas", encoder.encode("test1234"));
        andreas.getRoles().add(customer);
        userDao.save(andreas);

        User josefin = new User("Josefin", encoder.encode("test1234"));
        josefin.getRoles().add(customer);
        userDao.save(josefin);

        Order linnsOrder = new Order(linn);
        linnsOrder.getProducts().add(productMapper.toProductEntity(productService.getProductById(1L)));
        orderDao.save(linnsOrder);

        Order ylvasOrder = new Order(ylva);
        ylvasOrder.getProducts().add(productMapper.toProductEntity(productService.getProductById(2L)));
        orderDao.save(ylvasOrder);

        Order andreasOrder = new Order(andreas);
        andreasOrder.getProducts().add(productMapper.toProductEntity(productService.getProductById(3L)));
        orderDao.save(andreasOrder);

        Order josefinsOrder = new Order(josefin);
        josefinsOrder.getProducts().add(productMapper.toProductEntity(productService.getProductById(4L)));
        orderDao.save(josefinsOrder);

    }
}
