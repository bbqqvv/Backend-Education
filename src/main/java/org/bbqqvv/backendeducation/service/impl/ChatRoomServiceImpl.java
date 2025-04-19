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
import java.util.ArrayList;
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

        // 🔐 Kiểm tra quyền giáo viên
        if (!currentUser.getRoles().contains(Role.ROLE_TEACHER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 📦 Tìm học sinh theo lớp
        List<User> students = userRepository.findAllByStudentClass(request.getClassName());
        if (students.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        // 📌 Tạo danh sách memberIds (bao gồm giáo viên)
        List<String> memberIds = new ArrayList<>(students.stream().map(User::getId).toList());
        memberIds.add(currentUser.getId());

        // 🧱 Khởi tạo phòng chat
        ChatRoom room = ChatRoom.builder()
                .name(request.getName())
                .className(request.getClassName())
                .creatorId(currentUser.getId())
                .memberIds(memberIds)
                .createdAt(LocalDateTime.now())
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(room);

        // 👥 Lấy toàn bộ user info của members (gồm cả giáo viên)
        List<User> allMembers = new ArrayList<>(students);
        allMembers.add(currentUser);

        List<MemberInfoResponse> memberInfo = allMembers.stream()
                .map(userMapper::toMemberInfoResponse)
                .toList();

        return chatRoomMapper.toChatRoomResponse(savedRoom, memberInfo);
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
