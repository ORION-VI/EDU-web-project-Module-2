package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "registered_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    protected User() {
    }

    private User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Long getId() {
        return id;
    }

    public static User buildUser(String name, String email, Integer age) {
        return new User(name, email, age);
    }

    @Override
    public String toString() {
        return "User ID: " + id + '\n' +
                "User name: " + name + '\n' +
                "User email: " + email + '\n' +
                "User age: " + age + '\n' +
                "User created at: " + createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object instanceof User user) {
            return (this.getId().equals(user.getId())) &&
                   (this.getName().equals(user.getName())) &&
                   (this.getEmail().equals(user.getEmail())) &&
                   (this.getAge().equals(user.getAge()));
        }
        return false;
    }
}
