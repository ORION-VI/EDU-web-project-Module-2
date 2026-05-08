package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "registered_user")
public class User {

    //ID объекта, тип ссылочный для non-persistent состояния (null). Тип генерации - автоматический, на усмотрение Hibernate.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Имя пользователя, без null.
    @Column(nullable = false)
    private String name;

    //EMAIL пользователя, уникальное значение, без null.
    @Column(nullable = false, unique = true)
    private String email;

    //Сделал ссылочный на случай если юзер - новорожденный (смешно). Но вдруг! Пусть проверяет на non-null.
    @Column(nullable = false)
    private Integer age;

    //Timestamp persist объекта, не null, создается Hibernate'ом. Имя кастомное, т.к. синтаксис названий стобцов DB другой.
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    //Non-args конструктор, по просьбе Hibernate.
    protected User() {
    }

    //Приватный стандартный конструктор.
    private User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    //Сеттеры.
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

    //Геттеры.
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

    //Метод для создания объекта User (инкапсуляция).
    public static User buildUser(String name, String email, Integer age) {
        return new User(name, email, age);
    }

    //Переопределил для вывода данных.
    @Override
    public String toString() {
        return "User ID: " + id + '\n' +
                "User name: " + name + '\n' +
                "User email: " + email + '\n' +
                "User age: " + age + '\n' +
                "User created at: " + createdAt;
    }
}
