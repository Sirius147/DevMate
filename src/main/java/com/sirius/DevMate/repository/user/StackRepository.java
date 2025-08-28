package com.sirius.DevMate.repository.user;

import com.sirius.DevMate.domain.user.Stack;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StackRepository {

    private final EntityManager em;

    public Stack save(Stack stack) {
        if (stack.getStackId() == null) {
            em.persist(stack);
            return stack;
        } else {
            em.merge(stack);
            return stack;
        }
    }

    public List<Stack> findAllStack() {
        return em.createQuery("select s from Stack s order by s.id asc", Stack.class)
                .getResultList();
    }

    public void deleteByUser(User user) {
        em.createQuery("delete from Stack s where s.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    public List<Stack> findByUser(User user) {
        return em.createQuery("select s from Stack s where s.user = :user", Stack.class)
                .setParameter("user", user)
                .getResultList();
    }
}
