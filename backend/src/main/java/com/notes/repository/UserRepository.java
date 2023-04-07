package com.notes.repository;

import com.notes.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("{"
            + "$or: [{'?0': true}, {'active': true}],"
            + "$or: [{'firstName': {'$regex': '?1'}, 'lastName': {'$regex': '?1'}, 'email': {'$regex': '?1'}]}")
    Page<User> findAll(Boolean includeInactive, String filter, Pageable pageable);
}
