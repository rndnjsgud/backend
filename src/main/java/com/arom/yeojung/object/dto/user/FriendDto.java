package com.arom.yeojung.object.dto.user;

import com.arom.yeojung.object.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FriendDto {

  private User user1;

  private User user2;

}
