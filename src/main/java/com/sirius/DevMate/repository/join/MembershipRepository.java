package com.sirius.DevMate.repository.join;

import com.sirius.DevMate.domain.join.Membership;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MembershipRepository {
    
    private final EntityManager em;

    public Membership save(Membership newMembership) {
        if (newMembership.getMembershipId() == null) {
            em.persist(newMembership);
        } else {
            return em.merge(newMembership);
        }
        return newMembership;
    }

    public List<Membership> findByProjectId(Long projectId) {
        String jpql = "select m from Membership m where m.project.projectId = :projectId";
        return em.createQuery(jpql, Membership.class)
                .getResultList();
    }

    public boolean existByUserId(Long userId) {
        String jpql = "select count(m) from Membership m where m.user.userId = :userId";
        return (em.createQuery(jpql, Long.class)
                .getSingleResult() > 0);
    }
}
