package org.bbqqvv.backendeducation.mapper;

import org.bbqqvv.backendeducation.dto.response.ChatRoomResponse;
import org.bbqqvv.backendeducation.dto.response.MemberInfoResponse;
import org.bbqqvv.backendeducation.entity.ChatRoom;
import org.bbqqvv.backendeducation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom, List<MemberInfoResponse> members);

}
