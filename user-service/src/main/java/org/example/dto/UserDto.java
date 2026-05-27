package org.example.dto;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;

    public UserDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID: " + id + '\n' +
                "NAME: " + name + '\n' +
                "EMAIL: " + email + '\n' +
                "AGE: " + age + '\n';
    }
}
