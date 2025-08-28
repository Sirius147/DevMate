package com.sirius.DevMate.service.user;

import com.sirius.DevMate.config.security.controller.dto.BasicSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.DetailSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.StackSetUpDto;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.Stack;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.NotFoundUser;
import com.sirius.DevMate.repository.user.StackRepository;
import com.sirius.DevMate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final StackRepository stackRepository;

    // 로그인한 유저 불러오기
    public User getUser() throws NotFoundUser {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
        String registrationId = token.getAuthorizedClientRegistrationId(); // "google" | "github"
        OAuth2User principal = (OAuth2User) token.getPrincipal();
        Map<String, Object> attr = principal.getAttributes();

        String providerId = switch (registrationId) {
            case "google" -> String.valueOf(attr.get("sub")); // 항상 문자열
            case "github" -> String.valueOf(attr.get("id"));  // Integer/Long이므로 문자열로 변환
            default -> null;
        };

        if (providerId == null) throw new NotFoundUser("소셜 로그인 안된 유저입니다");
        OAuth2Provider provider = OAuth2Provider.valueOf(registrationId.toUpperCase());
        return userRepository.findByProviderAndProviderId(provider, providerId).get();

    }

    public Long getUserId() throws NotFoundUser {
        return getUser().getUserId();
    }

    // 닉네임 중복 여부 확인
    public boolean nickNameChecker(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
    // 기본 정보 셋팅
    public void setBasic(BasicSetUpDto basicSetUpDto) throws NotFoundUser {
        User user = getUser();
        user.setNickname(basicSetUpDto.getNickname());
        user.setRegion(basicSetUpDto.getRegions());
        user.setSkillLevel(basicSetUpDto.getSkillLevel());
    }
    // 추가 정보 셋팅
    public void setDetail(DetailSetUpDto detailSetUpDto) throws NotFoundUser {
        User user = getUser();
        user.setPreferredAtmosphere(detailSetUpDto.getPreferredAtmosphere());
        user.setPreferredCollaborateStyle(detailSetUpDto.getCollaborateStyle());
        user.setPosition(detailSetUpDto.getPosition());
    }

    public void setStack(List<StackSetUpDto> stackSetUpDtoList) throws NotFoundUser{
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

    public void removeStack(Stack stack) throws NotFoundUser {
        stackRepository.deleteByUser(getUser());
    }
}
