package com.notes.repository;

import com.notes.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, Long> {

    @Query("{'content': {'$regex': '?0'}}")
    Page<Note> findAll(String filter, Pageable pageable);
}
