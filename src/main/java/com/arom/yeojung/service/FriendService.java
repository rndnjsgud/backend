package com.arom.yeojung.service;

import com.arom.yeojung.object.dto.user.FriendRequest;
import com.arom.yeojung.object.dto.user.FriendStatus;
import com.arom.yeojung.object.dto.user.Friendship;
import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.user.FriendDto;
import com.arom.yeojung.object.dto.user.UserDto;
import com.arom.yeojung.repository.FriendRequestRepository;
import com.arom.yeojung.repository.FriendshipRepository;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;
  private final FriendRequestRepository friendRequestRepository;

  // 친구 요청 보내기
  @Transactional
  public void sendFriendRequest(Long senderId, Long receiverId) {
    User sender = userRepository.findByUserId(senderId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    User receiver = userRepository.findByUserId(receiverId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
      throw new CustomException(ErrorCode.ALREADY_REQUESTED_FRIENDSHIP);
    }

    FriendRequest request = FriendRequest.builder()
        .sender(sender)
        .receiver(receiver)
        .status(FriendStatus.PENDING)
        .build();
    friendRequestRepository.save(request);

    log.info("친구 요청을 보냈습니다. sender: {}, receiver: {}", senderId, receiverId);
  }

  // 친구 요청 수락
  @Transactional
  public void acceptFriendRequest(Long requestId) {
    FriendRequest request = friendRequestRepository.findByFriendRequestId(requestId)
        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

    request.setStatus(FriendStatus.ACCEPTED);
    friendRequestRepository.save(request);

    Friendship friendship = Friendship.builder()
        .user1(request.getSender())
        .user2(request.getReceiver())
        .build();
    friendshipRepository.save(friendship);

    log.info("친구 요청을 수락했습니다. sender: {}, receiver: {}", request.getSender().getUserId(), request.getReceiver().getUserId());
  }

  // 친구 요청 거절
  @Transactional
  public void rejectFriendRequest(Long requestId) {
    FriendRequest request = friendRequestRepository.findById(requestId)
        .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

    request.setStatus(FriendStatus.REJECTED);
    friendRequestRepository.save(request);

    log.info("친구 요청을 거절했습니다. sender: {}, receiver: {}", request.getSender().getUserId(), request.getReceiver().getUserId());
  }

  // 사용자에게 친구 요청 보낸 사용자 조회
  @Transactional(readOnly = true)
  public List<UserDto> getFriendRequest(Long userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    List<FriendRequest> requests = friendRequestRepository.findByReceiverAndStatus(user, FriendStatus.PENDING);

    return requests.stream().map(request -> UserDto.builder()
        .username(request.getSender().getUsername())
        .nickname(request.getSender().getNickname())
        .profileImageUrl(request.getSender().getProfileImageUrl())
        .build())
        .collect(Collectors.toList());
  }

  // 사용자의 친구 리스트
  @Transactional(readOnly = true)
  public List<FriendDto> getFriendList(Long userId) {
    List<Friendship> friends = friendshipRepository.findFriendshipByUserId(userId);
    return friends.stream().map(friend -> FriendDto.builder()
        .user1(friend.getUser1())
        .user2(friend.getUser2())
        .build())
        .collect(Collectors.toList());
  }
}
