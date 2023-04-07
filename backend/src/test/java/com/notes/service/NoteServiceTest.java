//package com.notes.service;
//
//import com.notes.api.NoteForm;
//import com.notes.model.Note;
//import com.notes.model.User;
//import com.notes.repository.NoteRepository;
//import com.notes.repository.UserRepository;
//import com.notes.security.services.Security;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Set;
//
//import static java.util.Optional.ofNullable;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class NoteServiceTest {
//
//    private static final Long USER_ID = 12313123123L;
//    private static final Long NOTE_ID = 321321321L;
//
//    @Mock
//    private UserService userService;
//    @Mock
//    private NoteService noteService;
//    @Mock
//    private NoteRepository noteRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private SequenceGeneratorService sequenceGeneratorService;
//    @Captor
//    private ArgumentCaptor<Note> noteArgumentCaptor;
//
//    @Test
//    public void should_like_note_if_unliked() {
//        try (MockedStatic<Security> utilities = mockStatic(Security.class)) {
//            when(noteRepository.findById(any())).thenReturn(ofNullable(initNote()));
//            when(userService.findById(USER_ID)).thenReturn(initUser());
//            utilities.when(Security::currentUserId).thenReturn(USER_ID);
//
//            noteService.likeNote(NOTE_ID.toString());
//
//            verify(noteRepository).save(noteArgumentCaptor.capture());
//
//            Note note = noteArgumentCaptor.getValue();
//
//            assertEquals(2, note.getLikes());
//
//            verify(userService, times(1)).findById(any());
//            verify(noteRepository, times(1)).findById(any());
//            verify(noteRepository, times(1)).save(any());
//            verifyNoMoreInteractions(userService, noteRepository, sequenceGeneratorService);
//        }
//    }
//
//    @Test
//    public void should_not_like_note_if_liked() {
//    }
//
//    @Test
//    public void should_unlike_note_if_liked() {
//
//    }
//
//    @Test
//    public void should_not_unlike_note_if_unliked() {
//
//    }
//
//    private User initUser() {
//        return User.builder()
//                .id(USER_ID)
//                .likedNotes(Set.of(NOTE_ID))
//                .build();
//    }
//
//    private Note initNote() {
//        return Note.builder()
//                .id(NOTE_ID)
//                .content("test")
//                .likes(1)
//                .build();
//    }
//
//    private NoteForm initNoteForm() {
//        return NoteForm.builder()
//                .content("test")
//                .build();
//    }
//}
