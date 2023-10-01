package com.helmes.cities.domain.user_role.entity;


import static javax.persistence.FetchType.LAZY;

import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
