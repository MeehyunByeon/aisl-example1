package aisl.example1.repository;

import aisl.example1.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final EntityManager em;
    public UserRepository(EntityManager em) {
        this.em = em;
    }
    public User save(User user) {
        em.persist(user);
        return user;
    }
    public Optional<User> findByName(String name) {
        User user = em.find(User.class, name);
        return Optional.ofNullable(user);
    }
    public List<User> findAll() {
        return em.createQuery("select m from User m", User.class)
                .getResultList();
    }

    public Optional<User> findByEmail(String email) {
        List<User> result = em.createQuery("select m from User m where m.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return result.stream().findAny();
    }

    public Optional<User> findByEmail2(String email) {
        User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    }

}