package com.Regestration.security.Repository;

import com.Regestration.security.Entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VerificationRepository extends JpaRepository<Verification,Long> {
}
