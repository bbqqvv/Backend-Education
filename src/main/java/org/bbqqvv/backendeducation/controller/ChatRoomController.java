package org.bbqqvv.backendeducation.controller;

import lombok.RequiredArgsConstructor;
import org.bbqqvv.backendeducation.dto.ApiResponse;
import org.bbqqvv.backendeducation.dto.request.ChatRoomRequest;
import org.bbqqvv.backendeducation.dto.response.ChatRoomResponse;
import org.bbqqvv.backendeducation.service.ChatRoomService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<ChatRoomResponse> createRoom(@RequestBody ChatRoomRequest request) {
        return ApiResponse.<ChatRoomResponse>builder()
                .success(true)
                .message("Tạo phòng chat thành công")
                .data(chatRoomService.create(request))
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<List<ChatRoomResponse>> getMyRooms() {
        return ApiResponse.<List<ChatRoomResponse>>builder()
                .success(true)
                .message("Danh sách phòng chat của bạn")
                .data(chatRoomService.getMyChatRooms())
                .build();
    }
}
