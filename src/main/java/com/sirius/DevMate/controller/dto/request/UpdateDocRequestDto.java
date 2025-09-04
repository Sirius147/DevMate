package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.RequestMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class UpdateDocRequestDto {
    private String name;
    private RequestMethod method;
    private String path;
    private String responseExample;
    private String parameter;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (!name.isBlank()) {
            map.put("name", name);
        }
        if (method != null) {
            map.put("method", method);
        }
        if (!path.isBlank()) {
            map.put("path", path);
        }
        if (!responseExample.isBlank()) {
            map.put("responseExample", responseExample);
        }
        if (!parameter.isBlank()) {
            map.put("parameter", parameter);
        }
        return map;
    }
}
