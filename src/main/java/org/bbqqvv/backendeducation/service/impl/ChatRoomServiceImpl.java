package org.bbqqvv.backendeducation.service.impl;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.config.jwt.SecurityUtils;
import org.bbqqvv.backendeducation.dto.request.ChatRoomRequest;
import org.bbqqvv.backendeducation.dto.response.ChatRoomResponse;
import org.bbqqvv.backendeducation.dto.response.MemberInfoResponse;
import org.bbqqvv.backendeducation.entity.ChatRoom;
import org.bbqqvv.backendeducation.entity.Role;
import org.bbqqvv.backendeducation.entity.User;
import org.bbqqvv.backendeducation.exception.AppException;
import org.bbqqvv.backendeducation.exception.ErrorCode;
import org.bbqqvv.backendeducation.mapper.ChatRoomMapper;
import org.bbqqvv.backendeducation.mapper.UserMapper;
import org.bbqqvv.backendeducation.repository.ChatRoomRepository;
import org.bbqqvv.backendeducation.repository.UserRepository;
import org.bbqqvv.backendeducation.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final UserMapper userMapper;

    @Override
    public ChatRoomResponse create(ChatRoomRequest request) {
        User currentUser = getAuthenticatedUser();

        if (!currentUser.getRoles().contains(Role.ROLE_TEACHER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<User> students = userRepository.findAllByStudentClass(request.getClassName());
        List<String> memberIds = students.stream().map(User::getId).toList();

        // Add giáo viên vào nhóm
        memberIds.add(currentUser.getId());

        ChatRoom room = ChatRoom.builder()
                .name(request.getName())
                .className(request.getClassName())
                .creatorId(currentUser.getId())
                .memberIds(memberIds)
                .createdAt(LocalDateTime.now())
                .build();

        ChatRoom saved = chatRoomRepository.save(room);

        // Convert User -> MemberInfoResponse
        List<MemberInfoResponse> memberInfo = students.stream()
                .map(userMapper::toMemberInfoResponse)
                .toList();

        return chatRoomMapper.toChatRoomResponse(saved, memberInfo);
    }

    @Override
    public List<ChatRoomResponse> getMyChatRooms() {
        User currentUser = getAuthenticatedUser();
        List<ChatRoom> rooms = chatRoomRepository.findAllByMemberIdsContaining(currentUser.getId());

        return rooms.stream().map(room -> {
            List<User> members = userRepository.findAllById(room.getMemberIds());
            List<MemberInfoResponse> memberInfo = members.stream()
                    .map(userMapper::toMemberInfoResponse)
                    .toList();
            return chatRoomMapper.toChatRoomResponse(room, memberInfo);
        }).toList();
    }

    private User getAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
