package com.arom.yeojung.object;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Follow extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long followId;

  // 팔로우 하는 사람 아이디
  @ManyToOne(fetch = FetchType.LAZY)
  private User followingId;

  // 팔로우 받는 사람 아이디
  @ManyToOne(fetch = FetchType.LAZY)
  private User followerId;
}
