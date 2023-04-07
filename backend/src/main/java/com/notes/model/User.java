package com.notes.model;

import com.notes.api.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(of = "id")
@Document(collection = "users")
public class User {

    @Transient
    public static final String USER_SEQUENCE = "users_sequence";

    @Id
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean active;
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();
    @Builder.Default
    private Set<Long> likedNotes = new HashSet<>();
}
