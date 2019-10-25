package com.learn.activitisample3.entity;

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
    private boolean active;

    public User() {
    }

    public User(String userIdentity, String userName ,boolean active) {
        this.userIdentity = userIdentity;
        this.userName = userName;
        this.active = active;
    }
}
