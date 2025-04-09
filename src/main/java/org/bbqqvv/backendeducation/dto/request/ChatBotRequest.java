package org.bbqqvv.backendeducation.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotRequest {
    private String message;
}
