package com.notes.model.mapper;

import com.notes.api.*;
import com.notes.model.Note;
import com.notes.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import static com.notes.utils.JavaUtils.getOrNull;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class ModelMapper {

    public static UserResponse toUserResponse(Page<User> page) {
        return UserResponse.builder()
                .total(page.getTotalElements())
                .items(page.getContent()
                        .stream()
                        .map(ModelMapper::toUserShort)
                        .collect(toList()))
                .build();
    }

    public static UserShortDto toUserShort(User entity) {
        return UserShortDto.builder()
                .email(entity.getEmail())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .active(entity.isActive())
                .build();
    }

    public static UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId().toString())
                .email(entity.getEmail())
                .lastName(entity.getLastName())
                .firstName(entity.getFirstName())
                .active(entity.isActive())
                .roles(entity.getRoles())
                .likedNotes(entity.getLikedNotes().stream()
                        .map(Object::toString)
                        .collect(toList()))
                .build();
    }

    public static NoteResponse toNoteResponse(Page<Note> page) {
        return NoteResponse.builder()
                .total(page.getTotalElements())
                .items(page.getContent()
                        .stream()
                        .map(ModelMapper::toNoteDto)
                        .collect(toList()))
                .build();
    }


    public static NoteDto toNoteDto(Note entity) {
        return NoteDto.builder()
                .id(entity.getId().toString())
                .content(entity.getContent())
                .user(getOrNull(entity.getUser(), ModelMapper::toUserShort))
                .likes(entity.getLikes())
                .build();
    }
}
