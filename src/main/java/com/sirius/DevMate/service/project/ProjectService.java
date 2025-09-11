package com.sirius.DevMate.service.project;

import com.sirius.DevMate.controller.dto.request.*;
import com.sirius.DevMate.controller.dto.response.*;
import com.sirius.DevMate.domain.common.project.*;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.join.Review;
import com.sirius.DevMate.domain.project.Doc;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.project.TodoList;
import com.sirius.DevMate.domain.project.chat.ChatChannel;
import com.sirius.DevMate.domain.project.chat.ChatMembership;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.repository.join.MembershipRepository;
import com.sirius.DevMate.repository.project.ProjectRepository;
import com.sirius.DevMate.repository.project.chat.ChatChannelRepository;
import com.sirius.DevMate.repository.project.chat.ChatMembershipRepository;
import com.sirius.DevMate.service.join.MembershipService;
import com.sirius.DevMate.service.user.NotificationService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final UserService userService;
    private final MembershipService membershipService;
    private final NotificationService notificationService;
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final ChatChannelRepository chatChannelRepository;
    private final ChatMembershipRepository chatMembershipRepository;
    private static final Integer LIST_SIZE = 10;

    public void newProject(NewProjectDto newProjectDto) throws UserNotFound, ProjectException {

        Integer recruitSize = newProjectDto.backendMembers() + newProjectDto.frontendMembers() +
                newProjectDto.pmMembers() + newProjectDto.designMembers();

        Integer currentSize = newProjectDto.currentBackend() + newProjectDto.currentFrontend() +
                newProjectDto.currentDesign() + newProjectDto.currentPm();


        if (newProjectDto.backendMembers() - newProjectDto.currentBackend() < 0) {
            throw new ProjectException("현재 백엔드 인원이 전체 백엔드 인원보다 많습니다!");
        }
        if (newProjectDto.frontendMembers() - newProjectDto.currentFrontend() < 0) {
            throw new ProjectException("현재 프론트 인원이 전체 프론트 인원보다 많습니다!");
        }
        if (newProjectDto.designMembers() - newProjectDto.currentDesign() < 0) {
            throw new ProjectException("현재 디자인 인원이 전체 디자인 인원보다 많습니다!");
        }
        if (newProjectDto.pmMembers() - newProjectDto.currentPm() < 0) {
            throw new ProjectException("현재 pm 인원이 전체 pm 인원보다 많습니다!");
        }

        Project newProject = Project
                .builder()
                .title(newProjectDto.title())
                .shortDescription(newProjectDto.shortDescription())
                .recruitSize(recruitSize)
                .currentSize(currentSize)
                .startDate(newProjectDto.startDate())
                .endDate(newProjectDto.endDate())
                .collaborateStyle(newProjectDto.collaborateStyle())
                .preferredRegion(newProjectDto.preferredRegion())
                .projectLevel(newProjectDto.projectLevel())
                .projectStatus(ProjectStatus.RECRUITING)
                .backendMembers(newProjectDto.backendMembers())
                .currentBackend(newProjectDto.currentBackend())
                .frontendMembers(newProjectDto.frontendMembers())
                .currentFrontend(newProjectDto.currentFrontend())
                .designMembers(newProjectDto.designMembers())
                .currentDesign(newProjectDto.currentDesign())
                .pmMembers(newProjectDto.pmMembers())
                .currentPm(newProjectDto.currentPm())
                .build();

        Membership newMembership = Membership
                .builder()
                .user(userService.getUser())
                .project(newProject)
                .membershipRole(MembershipRole.LEADER)
                .membershipStatus(MembershipStatus.PARTICIPATION)
                .build();
        membershipRepository.save(newMembership);
        newProject.getMemberships().add(newMembership);
        userService.getUser().getMyMemberships().add(newMembership);

        projectRepository.save(newProject);

    }

    public Project getProjectById(Long id) {
         return projectRepository.findById(id);
    }

    public List<Membership> getMemberships(Project project) {
        return membershipRepository.findByProjectId(project.getProjectId());
    }

    public PageList<AllProjectsResponseDto> showAllProjects(PageRequestDto pageRequestDto) {
       PageList<Project> projectPageList = projectRepository.findAll(pageRequestDto.page(),LIST_SIZE, pageRequestDto.sortBy());
        List<AllProjectsResponseDto> allProjectsResponseDtos = new ArrayList<>();
        for (Project project : projectPageList.content()) {
            allProjectsResponseDtos.add(
                    new AllProjectsResponseDto(
                            project.getProjectId(),
                            project.getTitle(),
                            project.getShortDescription(),
                            project.getRecruitSize(),
                            project.getCurrentSize(),
                            project.getStartDate(),
                            project.getEndDate(),
                            project.getCollaborateStyle(),
                            project.getPreferredRegion(),
                            project.getProjectLevel(),
                            project.getProjectStatus(),
                            project.getBackendMembers(),
                            project.getCurrentBackend(),
                            project.getFrontendMembers(),
                            project.getCurrentFrontend(),
                            project.getDesignMembers(),
                            project.getCurrentDesign(),
                            project.getPmMembers(),
                            project.getCurrentPm()
                    )
            );
        }
       return new PageList<>(allProjectsResponseDtos, projectPageList.totalCount());
    }

    public PageList<AllProjectsResponseDto> searchProject(ProjectSearchRequestDto projectSearchConditionDto) {
        PageList<Project> projectPageList = projectRepository.findByCondition(
                projectSearchConditionDto.getPage(),
                LIST_SIZE,
                "updatedAt",
                projectSearchConditionDto.getRegions(),
                projectSearchConditionDto.getCollaborateStyle(),
                projectSearchConditionDto.getSkillLevel(),
                projectSearchConditionDto.getProjectStatus());
        List<AllProjectsResponseDto> allProjectsResponseDtos = new ArrayList<>();
        for (Project project : projectPageList.content()) {
            allProjectsResponseDtos.add(
                    new AllProjectsResponseDto(
                            project.getProjectId(),
                            project.getTitle(),
                            project.getShortDescription(),
                            project.getRecruitSize(),
                            project.getCurrentSize(),
                            project.getStartDate(),
                            project.getEndDate(),
                            project.getCollaborateStyle(),
                            project.getPreferredRegion(),
                            project.getProjectLevel(),
                            project.getProjectStatus(),
                            project.getBackendMembers(),
                            project.getCurrentBackend(),
                            project.getFrontendMembers(),
                            project.getCurrentFrontend(),
                            project.getDesignMembers(),
                            project.getCurrentDesign(),
                            project.getPmMembers(),
                            project.getCurrentPm()
                    )
            );
        }
        return new PageList<>(allProjectsResponseDtos, projectPageList.totalCount());

    }

    public PageList<RecruitingProjectResponseDto> showRecruitingProjects() throws UserNotFound {
        User loginUser = userService.getUser();
        List<Membership> leaderMemberships = membershipService.getLeaderMembership(loginUser);
        if (leaderMemberships.isEmpty()) {
            return new PageList<>(null, 0L);
        } else {

            List<RecruitingProjectResponseDto> recruitingProjectResponseDtos = new ArrayList<>();
            for (Membership membership : leaderMemberships) {
                recruitingProjectResponseDtos.add(
                        new RecruitingProjectResponseDto(
                                membership.getProject().getProjectId(),
                                membership.getProject().getTitle(),
                                membership.getProject().getRecruitSize(),
                                membership.getProject().getCurrentSize(),
                                membership.getProject().getBackendMembers(),
                                membership.getProject().getCurrentBackend(),
                                membership.getProject().getFrontendMembers(),
                                membership.getProject().getCurrentFrontend(),
                                membership.getProject().getDesignMembers(),
                                membership.getProject().getCurrentDesign(),
                                membership.getProject().getPmMembers(),
                                membership.getProject().getCurrentPm(),
                                membership.getProject().getProjectStatus()
                        ));

            }

            return new PageList<>(recruitingProjectResponseDtos, (long) leaderMemberships.size());
        }
    }

    public PageList<TodoListDto> getTodoList(Long projectId) {
        Project project = projectRepository.findById(projectId);
        // 정렬 먼저 1순위 priority 2순위 endDate
        project.getTodoLists().sort(
                Comparator.comparing(TodoList::getPriority)
                        .thenComparing(TodoList::getEndDate)
        );

        List<TodoListDto> todoListDtos = new ArrayList<>();
        for (TodoList todoList : project.getTodoLists()) {
            todoListDtos.add(
                    new TodoListDto(
                            todoList.getToDoListId(),
                            todoList.getProject().getTitle(),
                            todoList.getTitle(),
                            todoList.getPosition(),
                            todoList.getContent(),
                            todoList.getPriority(),
                            todoList.getStartDate(),
                            todoList.getEndDate(),
                            todoList.isDone(),
                            todoList.getUpdatedAt()
                    )
            );
        }

        return new PageList<>(todoListDtos, (long) project.getTodoLists().size());
    }

    public PageList<DocDto> getDoc(Long projectId) {
        Project project = projectRepository.findById(projectId);
        List<DocDto> docDtos = new ArrayList<>();
        for (Doc doc : project.getDocs()) {
            docDtos.add(
                    new DocDto(
                            doc.getDocId(),
                            doc.getProject().getTitle(),
                            doc.getName(),
                            doc.getMethod(),
                            doc.getPath(),
                            doc.getResponseExample(),
                            doc.getParameter()
                    )
            );
        }
        return new PageList<>(docDtos, (long) docDtos.size());
    }

    public void createNewDoc(Long projectId, NewDocRequestDto newDocRequestDto) {

        Project project = projectRepository.findById(projectId);

        Doc newDoc = Doc.builder()
                .project(project)
                .name(newDocRequestDto.name())
                .method(newDocRequestDto.method())
                .path(newDocRequestDto.path())
                .responseExample(newDocRequestDto.responseExample())
                .parameter(newDocRequestDto.parameter())
                .build();

        projectRepository.saveProjectDoc(newDoc);
        project.getDocs().add(newDoc);
    }

    public void updateDoc(Long docId, Map<String, Object> requestMapper) {
        Doc updateDoc = projectRepository.findDocById(docId);

        if (requestMapper.containsKey("name")) {
            updateDoc.changeName((String) requestMapper.get("name"));
        }
        if (requestMapper.containsKey("path")) {
            updateDoc.changePath((String) requestMapper.get("path"));
        }
        if (requestMapper.containsKey("method")) {
            updateDoc.changeMethod((RequestMethod) requestMapper.get("method"));
        }
        if (requestMapper.containsKey("responseExample")) {
            updateDoc.changeResponseExample((String) requestMapper.get("responseExample"));
        }
        if (requestMapper.containsKey("parameter")) {
            updateDoc.changeParameter((String) requestMapper.get("parameter"));
        }



    }

    public void deleteDoc(Long projectId, Long docId) {
        Project project = projectRepository.findById(projectId);
        project.getDocs().remove(projectRepository.findDocById(docId));
        projectRepository.deleteDoc(docId);

    }

    public void createNewToDoList(Long projectId, NewToDoListRequestDto newToDoListRequestDto) {
        Project project = projectRepository.findById(projectId);
        TodoList newTodoList = TodoList.builder()
                .project(project)
                .title(newToDoListRequestDto.title())
                .position(newToDoListRequestDto.position())
                .content(newToDoListRequestDto.content())
                .priority(newToDoListRequestDto.priority())
                .startDate(newToDoListRequestDto.startDate())
                .endDate(newToDoListRequestDto.endDate())
                .done(newToDoListRequestDto.done())
                .build();

        projectRepository.saveProjectTodoList(newTodoList);
        project.getTodoLists().add(newTodoList);
    }

    public void updateTodoList(Long todoListId, Map<String, Object> map) {
        TodoList todoList = projectRepository.findTodoListById(todoListId);

        if (map.containsKey("title")) {
            todoList.changeTitle(map.get("title"));
        }
        if (map.containsKey("position")) {
            todoList.changePosition((Position) map.get("position"));
        }
        if (map.containsKey("content")) {
            todoList.changeContent((String) map.get("content"));
        }
        if (map.containsKey("priority")) {
            todoList.changePriority((Priority) map.get("priority"));
        }
        if (map.containsKey("startDate")) {
            todoList.changeStartDate((LocalDate) map.get("startDate"));
        }
        if (map.containsKey("endDate")) {
            todoList.changeEndDate((LocalDate) map.get("endDate"));
        }
        if (map.containsKey("done")) {
            todoList.changeDone((Boolean) map.get("done"));
        }



    }

    public void deleteTodoList(Long projectId, Long todoListId) {
        Project project = projectRepository.findById(projectId);
        project.getTodoLists().remove(projectRepository.findTodoListById(todoListId));
        projectRepository.deleteTodoList(todoListId);
    }

    public void completeProject(Long projectId) {
        Project project = projectRepository.findById(projectId);
        project.completeProject();
        ChatChannel chatChannel = project.getChatChannel();
        project.openChatChannel(null);
        // 챗 채널 페쇄
        chatChannelRepository.delete(chatChannel);
    }

    public void endRecruitingStartProject(Long projectId) {
        // 팀 챗 개설, project Status 변경
        Project project = projectRepository.findById(projectId);
        project.changeProjectStatus(ProjectStatus.IN_PROGRESS);
        membershipService.updateProjectMembershipStatus(project);


        ChatChannel chatChannel = ChatChannel.builder()
                .project(project)
                .name(project.getTitle())
                .size(project.getCurrentSize())
                .build();

        chatChannelRepository.save(chatChannel);
        project.openChatChannel(chatChannel);

        for (Membership membership : project.getMemberships()) {
            User chatUser = membership.getUser();
            ChatMembership newChatMembership = ChatMembership.builder()
                    .user(chatUser)
                    .chatChannel(chatChannel)
                    .build();

            chatMembershipRepository.save(newChatMembership);
            chatChannel.getChatMemberships().add(newChatMembership);

        }

        notificationService.notifyProjectStartNotification(project.getMemberships());


    }

    public PageList<ReviewResponseDto> getPeerReview(Long projectId) {
        Project project = projectRepository.findById(projectId);
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : project.getReviews()) {
            reviewResponseDtos.add(
                    new ReviewResponseDto(
                            review.getStar(),
                            review.getContent()
                    )
            );
        }
        return new PageList<>(reviewResponseDtos, (long) reviewResponseDtos.size());

    }

    public void peerReview(Long projectId, ReviewContentDto reviewContentDto) throws UserNotFound {
        User reviewer = userService.getUser();
        Project project = projectRepository.findById(projectId);
        Review newReview = Review.builder()
                .user(reviewer)
                .project(projectRepository.findById(projectId))
                .content(reviewContentDto.content())
                .star(reviewContentDto.star())
                .build();

        projectRepository.saveReview(newReview);
        project.getReviews().add(newReview);
        reviewer.getMyReviews().add(newReview);
    }
}
