package org.geli.marketplace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geli.marketplace.model.ActivityLogModel;
import org.geli.marketplace.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String activityType, String status, String description, Long referenceId, String tableName, Object request, Object response) {
        try {
            ActivityLogModel log = new ActivityLogModel();
            log.setActivityType(activityType);
            log.setStatus(status);
            log.setDescription(description);
            log.setReferenceId(referenceId);
            log.setTableName(tableName);

            if (request != null) {
                log.setRequestData(objectMapper.writeValueAsString(request));
            }

            if (response != null) {
                log.setResponseData(objectMapper.writeValueAsString(response));
            }

            activityLogRepository.save(log);
        } catch (Exception e) {
            // We don't want to crash the main transaction if logging fails, 
            // but we should at least print it.
            System.err.println("Failed to save activity log: " + e.getMessage());
        }
    }
}
