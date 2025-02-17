package com.arom.yeojung.repository;

import com.arom.yeojung.object.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
