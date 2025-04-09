    package org.bbqqvv.backendeducation.dto.request;

    import lombok.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.time.LocalDate;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class LeaveRequestRequest {
        private String senderName;
        private String recipient;
        private String reason;
        private String className;
        private MultipartFile imageFile;
        private LocalDate fromDate;
        private LocalDate toDate;
    }
