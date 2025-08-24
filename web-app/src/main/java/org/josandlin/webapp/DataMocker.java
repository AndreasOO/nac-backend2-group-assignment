package org.josandlin.webapp;

import org.josandlin.webapp.dao.OrderDao;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dao.RoleDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.entity.order.Order;
import org.josandlin.library.entity.user.Role;
import org.josandlin.library.entity.user.User;
import org.josandlin.library.mapper.product.ProductMapper;
import org.josandlin.webapp.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@org.springframework.core.annotation.Order(2)
public class DataMocker implements CommandLineRunner {

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
