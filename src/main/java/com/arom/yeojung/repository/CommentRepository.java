package com.arom.yeojung.repository;

import com.arom.yeojung.object.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
