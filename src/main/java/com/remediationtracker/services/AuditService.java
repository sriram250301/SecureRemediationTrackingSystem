package com.remediationtracker.services;

import com.remediationtracker.model.AuditLog;
import com.remediationtracker.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditRepo;

    public AuditService(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    public void log(String username, String action, String targetType, Long targetId) {

        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setTimestamp(LocalDateTime.now());

        auditRepo.save(log);
    }
}