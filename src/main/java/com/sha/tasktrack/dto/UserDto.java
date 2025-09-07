package com.sha.tasktrack.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer totalTasks;

    public UserDto(Long id, String name, String email) {
    }
}
