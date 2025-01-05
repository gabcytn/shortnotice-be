package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.service.BlocksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/{id}")
    public ResponseEntity<Void> blockById (@PathVariable UUID id, HttpServletRequest request) {
        return blocksService.blockById(request, id);
    }
}
