package com.arom.yeojung.object.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

  private String username;

  private String nickname;

  private String profileImageUrl;
}
