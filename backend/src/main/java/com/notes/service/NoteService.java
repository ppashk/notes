package com.notes.service;

import com.notes.api.NoteDto;
import com.notes.api.NoteForm;
import com.notes.api.NoteResponse;
import com.notes.model.Note;
import com.notes.model.User;
import com.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import static com.notes.api.I18N.ALREADY_LIKED;
import static com.notes.api.I18N.NOT_LIKED;
import static com.notes.model.Note.NOTE_SEQUENCE;
import static com.notes.model.mapper.ModelMapper.toNoteDto;
import static com.notes.model.mapper.ModelMapper.toNoteResponse;
import static com.notes.security.services.Security.currentUserId;
import static com.notes.utils.JavaUtils.emptyIfNull;
import static com.notes.utils.JavaUtils.parseStringSafe;
import static com.notes.utils.SpringUtils.checkArgumentCustom;
import static java.lang.Long.parseLong;
import static java.time.Instant.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public NoteResponse findAll(String filter, Pageable pageable) {
        Page<Note> notePage = noteRepository.findAll(emptyIfNull(filter), pageable);
        return toNoteResponse(notePage);
    }

    @Transactional
    public NoteDto createNote(NoteForm form) {
        Note note = Note.builder()
                .id(sequenceGeneratorService.generateSequence(NOTE_SEQUENCE))
                .content(form.getContent())
                .createdDate(now())
                .likes(0)
                .build();
        try {
            note.setUser(userService.findById(currentUserId()));
        } catch (Exception e) {
            note.setUser(null);
        }
        return toNoteDto(save(note));
    }

    @Transactional
    public NoteDto likeNote(String noteId) {
        User user = userService.findById(currentUserId());
        Note note = findById(parseLong(noteId));

        checkArgumentCustom(!user.getLikedNotes().contains(note.getId()), ALREADY_LIKED);

        note.setLikes(note.getLikes() + 1);
        user.getLikedNotes().add(parseLong(noteId));
        userService.save(user);

        return toNoteDto(save(note));
    }

    @Transactional
    public NoteDto dislikeNote(String noteId) {
        User user = userService.findById(currentUserId());
        Note note = findById(parseLong(noteId));

        checkArgumentCustom(user.getLikedNotes().contains(note.getId()), NOT_LIKED);

        note.setLikes(note.getLikes() - 1);
        user.getLikedNotes().remove(parseLong(noteId));
        userService.save(user);

        return toNoteDto(save(note));
    }

    public NoteDto getNoteById(String id) {
        return toNoteDto(findById(parseLong(id)));
    }

    private Note save(Note note) {
        return noteRepository.save(note);
    }

    private Note findById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND, "Note id: " + parseStringSafe(id)));
    }
}
