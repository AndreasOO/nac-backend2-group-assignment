package org.josandlin.library.dto;

public class UserSummaryDTO {
    Long id;
    String username;

    public UserSummaryDTO() {
    }

    public UserSummaryDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
