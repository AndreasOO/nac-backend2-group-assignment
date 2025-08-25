package org.josandlin.library.entity.product;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    public Long id;

    public String title;
    public double price;

    @Column(length = 1000)
    public String description;

    public String category;

    @Column(length = 1000)
    public String image;

    @AttributeOverrides({
            @AttributeOverride(name = "count", column = @Column(name = "rating_count"))
    })
    public Rating rating;

    public Product() {

    }

    public Product(String title, double price, String description, String category, String image, Rating rating) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.rating = rating;
    }

    public Product(Long id, String title, double price, String description, String category, String image, Rating rating) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
