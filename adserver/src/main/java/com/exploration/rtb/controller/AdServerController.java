package com.exploration.rtb.controller;

import com.exploration.rtb.dto.WinnerAdBid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/winning-ad")
public class AdServerController {

    private final RedisTemplate<String, WinnerAdBid> redisTemplate;

    public AdServerController(RedisTemplate<String, WinnerAdBid> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<WinnerAdBid> getWinningAd(@RequestParam String impressionId) {
        var key = impressionId + " winner";
        var winner = redisTemplate.opsForValue().get(key);
        if (winner == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(winner);
    }
}
