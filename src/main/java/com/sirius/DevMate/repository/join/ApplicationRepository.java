package com.sirius.DevMate.repository.join;

import com.sirius.DevMate.domain.join.Application;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
