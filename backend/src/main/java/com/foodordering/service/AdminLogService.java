package com.foodordering.service;

import com.foodordering.dto.response.AdminLogResponse;
import com.foodordering.entity.AdminLog;
import com.foodordering.entity.User;
import com.foodordering.repository.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private static final Logger log = LoggerFactory.getLogger(AdminLogService.class);

    private final AdminLogRepository adminLogRepository;

    public void log(User admin, String action, String details) {
        AdminLog adminLog = AdminLog.builder()
                .admin(admin)
                .action(action)
                .details(details)
                .build();
        adminLogRepository.save(adminLog);
        log.info("Admin activity [{}]: {} - {}", admin != null ? admin.getEmail() : "system", action, details);
    }

    public List<AdminLogResponse> getRecentLogs() {
        return adminLogRepository.findTop20ByOrderByCreatedAtDesc().stream()
                .map(AdminLogResponse::from)
                .collect(Collectors.toList());
    }
}
