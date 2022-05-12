package com.mandiri.repository;

import com.mandiri.entity.ErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorEntityRepository extends JpaRepository<ErrorEntity, String> {
}