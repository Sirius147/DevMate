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
                .setParameter("projectId",projectId)
                .getResultList();
    }

    public boolean existByUserId(Long userId, Long projectId) {
        String jpql = "select count(m) from Membership m " +
                "where m.user.userId = :userId and m.project.projectId = :projectId";
        return (em.createQuery(jpql, Long.class)
                .setParameter("projectId",projectId)
                .getSingleResult() > 0);
    }

    public Membership findByUserId(Long userId, Long projectId) {
        String jpql = "select m from Membership m where m.user.userId = :userId and m.project.projectId = :projectId";
        return em.createQuery(jpql, Membership.class)
                .setParameter("projectId", projectId)
                .getSingleResult();
    }
}
