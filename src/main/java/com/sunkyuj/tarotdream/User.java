package com.sunkyuj.tarotdream;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private Long id;
    private String name;
    private String password;
    private Timestamp created;

    @OneToMany(mappedBy = "user") // Post의 user
    private List<Dream> dreams = new ArrayList<>();
}
