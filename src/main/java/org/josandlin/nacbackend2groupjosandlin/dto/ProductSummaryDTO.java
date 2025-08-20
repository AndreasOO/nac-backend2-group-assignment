package org.josandlin.nacbackend2groupjosandlin.dto;

public class ProductSummaryDTO {

    public Long id;

    public String title;

    public double price;

    public ProductSummaryDTO() {

    }

    public ProductSummaryDTO(Long id, String title, double price) {
        this.id = id;
        this.title = title;
        this.price = price;
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
}
