package org.bbqqvv.backendeducation.service;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendLeaveRequestEmail(String to, Map<String, Object> model);
}
