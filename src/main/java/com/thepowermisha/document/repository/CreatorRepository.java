package com.thepowermisha.document.repository;

import com.thepowermisha.document.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, UUID> {
}
