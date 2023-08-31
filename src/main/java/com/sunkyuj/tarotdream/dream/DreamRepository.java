package com.sunkyuj.tarotdream.dream;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DreamRepository {
    private final EntityManager em;
    public void save(Dream dream) {
        em.persist(dream);
    }
    public Dream findById(Long dreamId){
        return em.find(Dream.class, dreamId);
    }

    public List<Dream> findAll() {
        return em.createQuery("select d from Dream d", Dream.class)
                .getResultList();
    }
}
