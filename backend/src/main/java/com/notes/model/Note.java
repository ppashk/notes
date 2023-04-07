package com.notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id")
@Document(collection = "notes")
public class Note {

    @Transient
    public static final String NOTE_SEQUENCE = "notes_sequence";

    @Id
    private Long id;
    private User user;
    private String content;
    private Integer likes;
}
