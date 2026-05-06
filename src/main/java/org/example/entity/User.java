package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
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

    @Column
    private int age;

    //Timestamp persist объекта, не null, создается Hibernate'ом. Имя кастомное, т.к. синтаксис названий стобцов DB другой.
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    //Non-args конструктор, по просьбе Hibernate.
    public User() {
    }

    //Стандартный конструктор.
    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    //Сеттеры.
    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //Геттеры.
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public String getEmail() {
        return email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
