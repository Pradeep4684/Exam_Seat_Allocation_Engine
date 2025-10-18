package com.mkcl.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mkcl.model.AppliedPost;

@Repository
public interface AppliedPostRepository extends JpaRepository<AppliedPost, Long> {
    List<AppliedPost> findByRegistrationNumber(Long reg);
}
