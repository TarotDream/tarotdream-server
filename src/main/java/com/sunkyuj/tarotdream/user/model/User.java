package com.sunkyuj.tarotdream.user.model;

import com.sunkyuj.tarotdream.dream.model.Dream;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    // TODO: createed, updated 추가
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    @Column(name = "created_at")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user") // Post의 user
    private List<Dream> dreams = new ArrayList<>();
}
