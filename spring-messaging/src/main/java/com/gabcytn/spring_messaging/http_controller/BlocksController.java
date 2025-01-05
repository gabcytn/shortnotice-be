package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.service.BlocksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlocksController {
    private final BlocksService blocksService;

    public BlocksController(BlocksService blocksService) {
        this.blocksService = blocksService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getBlocksList (HttpServletRequest request) {
        return blocksService.getBlocksList(request);
    }
}
