package com.sirius.DevMate.service.project;

import com.sirius.DevMate.controller.dto.NewProjectDto;
import com.sirius.DevMate.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void makeProject(NewProjectDto newProjectDto) {

    }
}
