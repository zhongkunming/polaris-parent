package com.github.polaris.sso.controller;

import com.github.polaris.common.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(description = "测试接口")
    @GetMapping("/api")
    public JsonResponse<Void> test() {
        return JsonResponse.ok();
    }
}
