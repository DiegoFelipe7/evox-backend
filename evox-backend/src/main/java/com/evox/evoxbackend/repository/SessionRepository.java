package com.evox.evoxbackend.repository;

import com.evox.evoxbackend.dto.SessionDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository  extends JpaRepository<SessionDto , Integer> {
}
