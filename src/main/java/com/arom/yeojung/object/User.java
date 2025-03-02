package com.arom.yeojung.object;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long userId;

  // 아이디
  @Column(unique = true, nullable = false)
  private String username;

  // 비밀번호
  @Column(nullable = false)
  private String password;

  // 닉네임
  @Column(unique = true, nullable = false)
  private String nickname;

  // 프로필 사진 URL
  private String profileImageUrl;

  // 팔로워 수
  private int followerCount;

  // 팔로워 증가
  public void increaseFollowerCount() {
    this.followerCount++;
  }

  // 팔로워 감소 (0 이하로 내려가지 않도록 처리)
  public void decreaseFollowerCount() {
    if (this.followerCount > 0) {
      this.followerCount--;
    }
  }
}
