package com.sirius.DevMate.repository.project;

import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.domain.common.project.ProjectStatus;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.project.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

    private final EntityManager em;

    public Project save(Project project) {
        if (project.getProjectId() == null) {
            em.persist(project);
        } else {
            return em.merge(project);
        }
        return project;
    }

    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(em.find(Project.class, id));
    }

    public PageList<Project> findAll(Integer page, Integer size, String sortBy) {
        String jpql = "select p from Project p order by p." +  sortBy + " acs";

        List<Project> rows = em.createQuery(jpql, Project.class)
                .setFirstResult(page*size) //OFFSET
                .setMaxResults(size)
                .getResultList();

        Long totalCount = em.createQuery("select count(p) from Project p", Long.class)
                .getSingleResult();

        return new PageList<>(rows, totalCount);
    }

    public PageList<Project> findByCondition(Integer page, Integer size, String sortBy,
                                             Regions regions, PreferredAtmosphere preferredAtmosphere,
                                             SkillLevel skillLevel, ProjectStatus projectStatus) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
        Root<Project> root = query.from(Project.class);

        List<Predicate> predicates = new ArrayList<>();

        if (regions != null) {
            predicates.add(criteriaBuilder.equal(root.get("regions"), regions));
        }
        if (preferredAtmosphere != null) {
            predicates.add(criteriaBuilder.equal(root.get("preferredAtmosphere"), preferredAtmosphere));
        }
        if (skillLevel != null) {
            predicates.add(criteriaBuilder.equal(root.get("skillLevel"), skillLevel));
        }
        if (projectStatus != null) {
            predicates.add(criteriaBuilder.equal(root.get("projectStatus"), projectStatus));
        }

        query.where(predicates.toArray(new Predicate[0]));

        Order order = criteriaBuilder.desc(root.get(sortBy));
        query.orderBy(order);

        List<Project> projects = em.createQuery(query)
                .setFirstResult(page * size) // OFFSET
                .setMaxResults(size)
                .getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Project> countRoot = query.from(Project.class);
        countQuery.select(criteriaBuilder.count(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();

        if (regions != null) {
            countPredicates.add(criteriaBuilder.equal(countRoot.get("regions"), regions));
        }
        if (preferredAtmosphere != null) {
            countPredicates.add(criteriaBuilder.equal(countRoot.get("preferredAtmosphere"), preferredAtmosphere));
        }
        if (skillLevel != null) {
            countPredicates.add(criteriaBuilder.equal(countRoot.get("skillLevel"), skillLevel));
        }
        if (projectStatus != null) {
            countPredicates.add(criteriaBuilder.equal(countRoot.get("projectStatus"), projectStatus));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long totalCount = em.createQuery(countQuery)
                .getSingleResult();

        return new PageList<>(projects, totalCount);
    }

}
