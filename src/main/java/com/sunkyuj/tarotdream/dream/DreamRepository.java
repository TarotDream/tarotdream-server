package com.sunkyuj.tarotdream.dream;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DreamRepository {
    private final EntityManager em;
    public void save(Dream dream) {
        em.persist(dream);
    }

}
