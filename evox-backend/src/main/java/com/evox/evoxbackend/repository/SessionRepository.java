package com.evox.evoxbackend.repository;

import com.evox.evoxbackend.model.Session;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository  extends ReactiveCrudRepository<Session, Integer> {
}
