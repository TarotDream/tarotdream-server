package com.sunkyuj.tarotdream.user;

import com.sunkyuj.tarotdream.user.model.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }
    public User findByName(String name) {
        return em.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList().get(0);
    }
}
