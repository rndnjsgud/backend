package com.arom.yeojung.repository;

import com.arom.yeojung.object.Follow;
import com.arom.yeojung.object.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsByFollowerAndFollowee(User follower, User followee);

  Optional<Follow> findByFollowerAndFollowee(User follower, User followee);

  Page<Follow> findAllByFollowee(User followee, Pageable pageable);

  Page<Follow> findAllByFollower(User follower, Pageable pageable);

}
