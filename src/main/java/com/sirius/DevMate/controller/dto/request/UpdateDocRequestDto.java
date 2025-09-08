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
        if (name != null) {
            map.put("name", name);
        }
        if (method != null) {
            map.put("method", method);
        }
        if (path != null) {
            map.put("path", path);
        }
        if (responseExample != null) {
            map.put("responseExample", responseExample);
        }
        if (parameter != null) {
            map.put("parameter", parameter);
        }
        return map;
    }
}
