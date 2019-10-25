package com.learn.activitisample3.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "entity_dept")
@Data
@DynamicUpdate
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long deptId;
    @Column(unique = true,nullable = false)
    String deptIdentity;
    @Column(nullable = false)
    String deptName;
    private boolean active;

    public Dept(){
    }

    public Dept(String deptIdentity, String deptName) {
        this.deptIdentity = deptIdentity;
        this.deptName = deptName;
    }
}
