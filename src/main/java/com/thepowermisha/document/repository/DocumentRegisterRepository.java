package com.thepowermisha.document.repository;


import com.thepowermisha.document.entity.DocumentRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRegisterRepository extends JpaRepository<DocumentRegister, UUID> {
}
