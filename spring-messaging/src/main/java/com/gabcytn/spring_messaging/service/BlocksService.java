package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.repository.BlocksRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlocksService {
    private final BlocksRepository blocksRepository;

    public BlocksService(BlocksRepository blocksRepository) {
        this.blocksRepository = blocksRepository;
    }

    public ResponseEntity<List<User>> getBlocksList (HttpServletRequest request) {
        try {
            final List<User> blockList = blocksRepository.findByBlockerId(getRequesterUUID(request));
            return new ResponseEntity<>(blockList, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error getting block list");
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UUID getRequesterUUID (HttpServletRequest request) {
        return UUID.fromString((String) request.getSession().getAttribute("uuid"));
    }
}
