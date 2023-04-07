package com.notes.web;

import com.notes.api.*;
import com.notes.security.jwt.JwtUtils;
import com.notes.service.NoteService;
import com.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.notes.utils.SpringUtils.buildPageable;

@SuppressWarnings({"PMD.ExcessivePublicCount"})
@Component
@Slf4j
@RequiredArgsConstructor
public class Controller implements NotesApi {

    private final UserService userService;
    private final NoteService noteService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public String generateToken(CredentialForm form) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    @Override
    public UserDto getUserById(String id) {
        return userService.getUserById(id);
    }

    @Override
    public UserResponse getUsers(Boolean includeInactive, Integer page, Integer size, String order, String sort, String filter) {
        Pageable pageable = buildPageable(page, size, order, sort, "firstName");
        return userService.findAll(includeInactive, filter, pageable);
    }

    @Override
    public void registerUser(UserRegistrationForm form) {
        userService.registerUser(form);
    }

    @Override
    public UserDto createUser(UserRegistrationForm form) {
        return userService.createUser(form);
    }

    @Override
    public UserDto updateUser(UserForm form, String id) {
        return userService.updateUser(id, form);
    }

    @Override
    public NoteResponse getNotes(Integer page, Integer size, String order, String sort, String filter) {
        Pageable pageable = buildPageable(page, size, order, sort, "id");
        return noteService.findAll(filter, pageable);
    }

    @Override
    public NoteDto getNoteById(String id) {
        return noteService.getNoteById(id);
    }

    @Override
    public NoteDto createNote(NoteForm form) {
        return noteService.createNote(form);
    }

    @Override
    public NoteDto likeNote(String id) {
        return noteService.likeNote(id);
    }

    @Override
    public NoteDto dislikeNote(String id) {
        return noteService.dislikeNote(id);
    }
}

