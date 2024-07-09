package com.ohgiraffers.blog.jinhee.restcontroller;

import com.ohgiraffers.blog.jinhee.service.JinheeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jinhee/postpage")
public class BlogRestController {

    private final JinheeService jinheeService;

    @Autowired
    public BlogRestController(JinheeService jinheeService) {
        this.jinheeService = jinheeService;
    }

    // 좋아요 기능 처리
    @PostMapping("/postpage/{id}/likepost")
    public ResponseEntity<Void> likePost(@PathVariable("id") Long id) {
        jinheeService.likePost(id); // 서비스 계층에서 좋아요 기능을 수행
        return ResponseEntity.ok().build(); // 성공적인 응답 반환
    }

    // 좋아요 수 조회
    @GetMapping("/postpage/{id}/likecount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable("id") Long id) {
        int likeCount = jinheeService.getLikes(id); // 서비스 계층에서 좋아요 수 조회
        return ResponseEntity.ok(likeCount); // 조회된 좋아요 수를 클라이언트에 반환
    }
}