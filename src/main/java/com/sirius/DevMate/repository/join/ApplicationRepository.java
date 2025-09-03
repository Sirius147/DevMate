package com.sirius.DevMate.repository.join;

import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.domain.join.Application;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationRepository {

    private final EntityManager em;

    public Application save(Application application) {
        if (application.getApplicationId() == null) {
            em.persist(application);
        } else {
            return em.merge(application);
        }
        return application;
    }

    public Application findById(Long applicationId) {
        return em.find(Application.class, applicationId);
    }

    public List<Application> findByUserId(Long userId) {
        String jpql = "select a from Application a where a.user.userId = :userId";
        List<Application> applications = em.createQuery(jpql, Application.class)
                .getResultList();
        return applications;
    }

    public void delete(Application application) {
        em.remove(em.contains(application)? application : em.merge(application));
    }
}
