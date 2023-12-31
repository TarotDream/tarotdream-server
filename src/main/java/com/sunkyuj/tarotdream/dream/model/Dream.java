package com.sunkyuj.tarotdream.dream.model;

import com.sunkyuj.tarotdream.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

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

    private String dreamTitle;
    private String engDreamTitle;

    @Lob
    @Column(columnDefinition = "text")
    private String imageUrl;

    @ElementCollection
    private List<String> possibleMeanings;

    private String recommendedTarotCard;
    private Timestamp created;
    private Timestamp updated;

    // TODO: createed, updated 추가
//    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
//    @Column(name = "created_at")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
//    private LocalDateTime updatedAt = LocalDateTime.now();

}
