package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.project.Priority;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class UpdateTodoListRequestDto {
    private String title;
    private Position position;
    @Size(min = 5, max = 300)
    private String content;
    private Priority priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean done;

    public Map<String, Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        if (title != null) {
            map.put("title", title);
        }
        if (position != null) {
            map.put("position", position);
        }
        if (content != null) {
            map.put("content", content);
        }
        if (priority != null) {
            map.put("priority", priority);
        }
        if (startDate != null) {
            map.put("startDate", startDate);
        }
        if (endDate != null) {
            map.put("endDate", endDate);
        }
        if (done != null) {
            map.put("done", done);
        }
        return map;






    }
}
