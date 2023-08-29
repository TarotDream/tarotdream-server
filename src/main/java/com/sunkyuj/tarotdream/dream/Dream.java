package com.sunkyuj.tarotdream.dream;

import com.sunkyuj.tarotdream.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dream_id")
    private Long dreamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK, 연관관계의 주인
    private User user;

    private String title;
    private Timestamp created;
    private String content;
    private String imageUrl;



}
