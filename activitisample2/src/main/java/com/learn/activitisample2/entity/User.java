package com.learn.activitisample2.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Table(name = "entity_user")
@Entity
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    @Column(unique = true,nullable = false)
    private String userIdentity;
    @Column(nullable = false)
    private String userName;

    public User() {
    }

    public User(String userIdentity, String userName) {
        this.userIdentity = userIdentity;
        this.userName = userName;
    }
}
