package com.arom.yeojung.repository;

import com.arom.yeojung.object.dto.user.Friendship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  @Query(value = """
      SELEC * FROM friendship
      WHERE user1_id = :userId OR user2_id = :userId"""
      , nativeQuery = true)
  List<Friendship> findFriendshipByUserId(@Param("userId") Long userId);
}