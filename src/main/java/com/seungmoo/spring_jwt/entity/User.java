package com.seungmoo.spring_jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String nickname;

    @JsonIgnore
    private boolean activated;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Authority> authorities = new HashSet<>();
}
