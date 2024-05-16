package com.beside.archivist.controller.home;

import com.beside.archivist.dto.home.HomeFeedDto;
import com.beside.archivist.dto.home.HomeFeedInfoDto;
import com.beside.archivist.entity.home.HomeFeed;
import com.beside.archivist.service.home.HomeFeedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeFeedService homeFeedServiceImpl;

    @PostMapping("/home")
    @Operation(summary = "홈 피드 요청 API")
    public ResponseEntity<?> saveHomeFeed(@RequestBody HomeFeedDto homeFeedDto) {
        homeFeedServiceImpl.saveHomeFeed(homeFeedDto);
        return ResponseEntity.ok().body("등록이 완료되었습니다.");
    }

    @GetMapping("/home-{type}")
    @Operation(summary = "홈 피드 데이터 응답 API")
    public ResponseEntity<?> getHomeFeedWithLink(@PathVariable("type") String type) {
        List<HomeFeedInfoDto<?>> response =
                homeFeedServiceImpl.getHomeFeedByType(HomeFeed.Type.valueOf(type.toUpperCase()));
        return ResponseEntity.ok().body(response);
    }
}
