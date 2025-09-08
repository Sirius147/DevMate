package com.sirius.DevMate.repository.project;

import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.domain.common.project.ProjectStatus;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.join.Review;
import com.sirius.DevMate.domain.project.Doc;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.project.TodoList;
import com.sirius.DevMate.domain.project.chat.ChatChannel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public Doc saveProjectDoc(Doc doc) {
        if (doc.getDocId() == null) {
            em.persist(doc);
        } else {
            return em.merge(doc);
        }
        return doc;
    }

    public Project findById(Long id) {
        return em.find(Project.class, id);
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

    public Doc findDocById(Long docId) {
        return em.find(Doc.class, docId);
    }

    public void deleteDoc(Long docId) {
        Doc doc = findDocById(docId);
        em.remove(em.contains(doc) ? doc : em.merge(doc));
    }

    public void saveProjectTodoList(TodoList newTodoList) {
        if (newTodoList.getToDoListId() == null) {
            em.persist(newTodoList);
        } else {
            em.merge(newTodoList);
        }
    }

    public TodoList findTodoListById(Long todoListId) {
        return em.find(TodoList.class, todoListId);
    }

    public void deleteTodoList(Long todoListId) {
        TodoList todoList = findTodoListById(todoListId);
        em.remove(em.contains(todoList) ? todoList : em.merge(todoList));
    }

    public void saveReview(Review newReview) {
        if (newReview.getReviewId() == null) {
            em.persist(newReview);
        } else {
            em.merge(newReview);
        }
    }

    public ChatChannel findChatChannelByProjectId(Long projectId) {
        String jpql = "select c from ChatChannel c where c.project.projectId = :projectId";
        return em.createQuery(jpql, ChatChannel.class)
                .setParameter("projectId", projectId)
                .getResultList()
                .getFirst();
    }

    public ChatChannel findChatChannelByChatChannelId(Long chatChannelId) {
        return em.find(ChatChannel.class, chatChannelId);
    }
}
