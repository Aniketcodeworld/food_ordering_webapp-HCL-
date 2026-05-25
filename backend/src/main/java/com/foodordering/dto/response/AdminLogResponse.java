package com.foodordering.dto.response;

import com.foodordering.entity.AdminLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminLogResponse {
    private Long id;
    private String adminEmail;
    private String action;
    private String details;
    private LocalDateTime createdAt;

    public static AdminLogResponse from(AdminLog log) {
        return AdminLogResponse.builder()
                .id(log.getId())
                .adminEmail(log.getAdmin() != null ? log.getAdmin().getEmail() : "system")
                .action(log.getAction())
                .details(log.getDetails())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
