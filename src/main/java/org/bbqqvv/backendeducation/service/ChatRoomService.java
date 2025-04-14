package org.bbqqvv.backendeducation.service;

import org.bbqqvv.backendeducation.dto.request.ChatRoomRequest;
import org.bbqqvv.backendeducation.dto.response.ChatRoomResponse;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponse create(ChatRoomRequest request);

    List<ChatRoomResponse> getMyChatRooms();
}
