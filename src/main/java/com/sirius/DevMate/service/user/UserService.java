package com.sirius.DevMate.service.user;

import com.sirius.DevMate.config.security.controller.dto.request.BasicSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.request.DetailSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.request.StackSetUpDto;
import com.sirius.DevMate.controller.dto.request.UpdateProfileDto;
import com.sirius.DevMate.controller.dto.response.MembershipDto;
import com.sirius.DevMate.controller.dto.response.MyPageDto;
import com.sirius.DevMate.controller.dto.response.NotificationDto;
import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.common.user.StackType;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.user.Notification;
import com.sirius.DevMate.domain.user.Stack;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.repository.user.StackRepository;
import com.sirius.DevMate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StackRepository stackRepository;

    // 로그인한 유저 불러오기
    public User getUser() throws UserNotFound {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            throw new UserNotFound("인증되지 않았습니다.");
        }

        // 1) 일반 API 요청: JWT 인증
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            // 기본 설정이면 getName() == JWT의 sub
            String sub = jwtAuth.getName(); // 또는: jwtAuth.getToken().getSubject()
            Long userId = Long.valueOf(sub);
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound("사용자를 찾을 수 없습니다."));
        } else {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
            String registrationId = token.getAuthorizedClientRegistrationId(); // "google" | "github"
            OAuth2User principal = (OAuth2User) token.getPrincipal();
            Map<String, Object> attr = principal.getAttributes();

            String providerId = switch (registrationId) {
                case "google" -> String.valueOf(attr.get("sub")); // 항상 문자열
                case "github" -> String.valueOf(attr.get("id"));  // Integer/Long이므로 문자열로 변환
                default -> null;
            };

            if (providerId == null) throw new UserNotFound("소셜 로그인 안된 유저입니다");
            OAuth2Provider provider = OAuth2Provider.valueOf(registrationId.toUpperCase());
            return userRepository.findByProviderAndProviderId(provider, providerId).get();
        }

    }

    public Long getUserId() throws UserNotFound {
        return getUser().getUserId();
    }

    // 닉네임 중복 여부 확인
    public boolean isNickNameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
    // 기본 정보 셋팅
    public void setBasic(BasicSetUpDto basicSetUpDto) throws UserNotFound {
        User user = getUser();
        user.setNickname(basicSetUpDto.getNickname());
        user.setRegion(basicSetUpDto.getRegions());
        user.setSkillLevel(basicSetUpDto.getSkillLevel());
    }
    // 추가 정보 셋팅
    public void setDetail(DetailSetUpDto detailSetUpDto) throws UserNotFound {
        User user = getUser();
        user.setPreferredAtmosphere(detailSetUpDto.getPreferredAtmosphere());
        user.setPreferredCollaborateStyle(detailSetUpDto.getCollaborateStyle());
        user.setPosition(detailSetUpDto.getPosition());
    }

    public void setStack(List<StackSetUpDto> stackSetUpDtoList) throws UserNotFound {
        User user = getUser();

        if (stackSetUpDtoList == null) return;

        for (StackSetUpDto s : stackSetUpDtoList) {
            Stack newStack = Stack.builder()
                    .stackName(s.getStackName())
                    .stackType(s.getStackType())
                    .user(user)
                    .build();
            stackRepository.save(newStack);
            user.getStacks().add(newStack);
        }
    }

    public void removeStack(Stack stack) throws UserNotFound {
        User loginUser = getUser();
        stackRepository.deleteByUser(loginUser);
        loginUser.getStacks().remove(stack);
    }

    public MyPageDto getMyPage() throws UserNotFound {
        User loginUser = getUser();
        return new  MyPageDto(loginUser.getName(),
                loginUser.getEmail(),
                loginUser.getNickname(),
                loginUser.getIntro(),
                loginUser.getRegion(),
                loginUser.getSkillLevel(),
                loginUser.getPopularity(),
                loginUser.getPreferredAtmosphere(),
                loginUser.getPreferredCollaborateStyle(),
                loginUser.getPosition(),
                loginUser.getStacks());
    }

    public void setProfile(UpdateProfileDto updateProfileDto) throws UserNotFound {
        User user = getUser();
        user.setNickname(updateProfileDto.nickname());
        user.setIntro(updateProfileDto.intro());
        user.setRegion(updateProfileDto.regions());
        user.setSkillLevel(updateProfileDto.skillLevel());
        user.setPreferredCollaborateStyle(updateProfileDto.collaborateStyle());
        user.setPosition(updateProfileDto.position());

        for (StackSetUpDto stackSetUpDto : updateProfileDto.stackSetUpDtos()) {
            switch (stackSetUpDto.getStackType()) {
                case StackType.LANGUAGE -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.LANGUAGE);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.LANGUAGE, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.LANGUAGE)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
                case StackType.DB -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.DB);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.DB, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.LANGUAGE)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
                case StackType.FRAMEWORK -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.FRAMEWORK);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.FRAMEWORK, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.FRAMEWORK)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
                case StackType.IDE -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.IDE);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.IDE, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.IDE)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
                case StackType.DEPLOY -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.DEPLOY);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.DEPLOY, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.DEPLOY)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
                case StackType.DESIGN -> {
                    Optional<Stack> stack = stackRepository.findSpecificStack(user, StackType.DESIGN);
                    if (stack.isPresent()) {
                        stackRepository.setSpecificStack(user, StackType.DESIGN, stackSetUpDto.getStackName());
                    } else {
                        Stack newStack = Stack.builder()
                                .stackType(StackType.DESIGN)
                                .stackName(stackSetUpDto.getStackName())
                                .user(user)
                                .build();
                        stackRepository.save(newStack);
                        user.getStacks().add(newStack);
                    }
                }
            }
        }


    }

    public List<MembershipDto> getMemberShips() throws UserNotFound {
        User user = getUser();
        List<Membership> memberships = user.getMyMemberships();
        List<MembershipDto> membershipDtos = new ArrayList<>();
        for (Membership membership : memberships) {
            membershipDtos.add(new MembershipDto(
                    membership.getProject().getTitle(),
                    membership.getProject().getProjectStatus(),
                    membership.getMembershipStatus(),
                    membership.getMembershipRole()));
        }

        return membershipDtos;

    }

    public boolean notifyUserNotifications() throws UserNotFound {
        User loginUser = getUser();
        List<Notification> notifications = loginUser.getNotifications();
        for (Notification notification : notifications) {
            // 안 읽은 알람
            if (!notification.getChecked()) {
                return true;
            }
        }
        return  false;
    }

    public PageList<NotificationDto> getNotification() throws UserNotFound {
        List<Notification> notifications = getUser().getNotifications();
        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            // 안 읽은 알림
            if (!notification.getChecked()) {

                notificationDtos.add(new NotificationDto(
                        notification.getNotificationType(),
                        notification.getContent(),
                        notification.getCreatedAt()));
                // 읽음 처리
                notification.checked();
            }

//            } // 읽은 알림 -> 삭제 처리
//            else {
//                userRepository.expireNotification(notification);
//            }
        }

        return new PageList<>(notificationDtos, (long) notificationDtos.size());
    }

}
