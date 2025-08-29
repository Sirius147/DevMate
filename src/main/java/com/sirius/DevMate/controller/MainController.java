package com.sirius.DevMate.controller;

import com.sirius.DevMate.controller.dto.NewProjectDto;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final ProjectService projectService;
//    private final MemberShipService memberShipService;

    @GetMapping
    public List<Project> mainPage() {

        return null;
    }

    @GetMapping("/new-project")
    public ResponseEntity<Void> newProject() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/new-project")
    public ResponseEntity<Void> projectMade(NewProjectDto newProjectDto) {
        projectService.makeProject(newProjectDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
