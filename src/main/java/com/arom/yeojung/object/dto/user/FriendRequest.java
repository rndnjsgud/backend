package com.arom.yeojung.object.dto.user;

import com.arom.yeojung.object.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long friendRequestId;

  @ManyToOne
  @NotNull
  private User sender;

  @ManyToOne
  @NotNull
  private User receiver;

  @Enumerated(EnumType.STRING)
  private FriendStatus status;

}
