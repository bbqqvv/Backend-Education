package org.bbqqvv.backendeducation.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private String id;
    private String name;
    private String className;
    private List<MemberInfoResponse> members;
}
