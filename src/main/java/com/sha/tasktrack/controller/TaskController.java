package com.sha.tasktrack.controller;

import com.sha.tasktrack.dto.TaskDto;
import com.sha.tasktrack.entity.User;
import com.sha.tasktrack.repository.UserRepository;
import com.sha.tasktrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private final TaskService taskService;

    @Autowired
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto dto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(taskService.create(dto, user.getId()));
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> list(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the logged-in user by email
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(taskService.getById(id, user.getId()));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<TaskDto>> search(@RequestParam String title,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(taskService.searchByTitle(title, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id,
                                          @RequestBody TaskDto dto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(taskService.update(id, dto, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        taskService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}
