package com.notes.service;

import com.notes.api.UserDto;
import com.notes.api.UserForm;
import com.notes.api.UserRegistrationForm;
import com.notes.api.UserResponse;
import com.notes.model.User;
import com.notes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.regex.Pattern;

import static com.notes.api.I18N.*;
import static com.notes.api.UserRole.ADMIN;
import static com.notes.api.UserRole.USER;
import static com.notes.model.User.USER_SEQUENCE;
import static com.notes.model.mapper.ModelMapper.toUserDto;
import static com.notes.model.mapper.ModelMapper.toUserResponse;
import static com.notes.security.services.Security.currentUserId;
import static com.notes.security.services.Security.isAdmin;
import static com.notes.utils.JavaUtils.*;
import static com.notes.utils.SpringUtils.checkArgumentCustom;
import static java.lang.Long.parseLong;
import static java.util.Set.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z]).{6,64}");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceGeneratorService sequenceGeneratorService;


    public void registerUser(UserRegistrationForm form) {
        validateUserOnCreate(form);
        User user = mapUser(form);
        save(user);
    }

    public UserDto createUser(UserRegistrationForm form) {
        validateUserOnCreate(form);
        User user = mapUser(form);
        if (falseIfNull(form.getIsAdmin())) {
            user.getRoles().add(ADMIN);
        }
        return toUserDto(save(user));
    }

    public UserDto updateUser(String id, UserForm form) {
        User user = findById(parseLong(id));
        updateUserFields(form, user);
        return toUserDto(save(user));
    }

    public UserResponse findAll(Boolean includeInactive, String filter, Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(includeInactive, emptyIfNull(filter), pageable);
        return toUserResponse(usersPage);
    }

    public UserDto getUserById(String id) {
        User user = findById(parseLongSafe(id));
        checkArgumentCustom(user.getId().equals(currentUserId()) || isAdmin(), NO_AUTHORITIES_TO_VIEW_THIS_USER);
        return toUserDto(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND, "User id: " + parseStringSafe(id)));
    }

    private void validateUserOnCreate(UserRegistrationForm form) {
        checkArgumentCustom(isNotBlank(form.getEmail()) && isValidMailAddress(form.getEmail()), WRONG_FORMAT_OF_EMAIL);
        checkArgumentCustom(isEmailNotRegistered(form.getEmail()), EMAIL_ALREADY_EXISTS);
        checkArgumentCustom(isNotBlank(form.getPassword()), PASSWORD_REQUIRED);
        checkArgumentCustom(PASSWORD_PATTERN.matcher(form.getPassword()).matches(), WRONG_FORMAT_OF_PASSWORD);
        checkArgumentCustom(form.getPassword().equals(form.getRepeatPassword()), PASSWORD_DOES_NOT_MATCH);
        checkArgumentCustom(validName(form.getFirstName()), FIRSTNAME_REQUIRED);
        checkArgumentCustom(validName(form.getLastName()), LASTNAME_REQUIRED);
    }

    private boolean isEmailNotRegistered(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    private static boolean validName(String name) {
        return isNotBlank(name) && name.length() > 1 && name.length() < 65;
    }

    private User mapUser(UserRegistrationForm form) {
        return User.builder()
                .id(sequenceGeneratorService.generateSequence(USER_SEQUENCE))
                .email(trimToNull(form.getEmail()))
                .firstName(trimToNull(form.getFirstName()))
                .lastName(trimToNull(form.getLastName()))
                .password(passwordEncoder.encode(trimToNull(form.getPassword())))
                .active(true)
                .roles(of(USER))
                .build();
    }

    private void updateUserFields(UserForm form, User user) {
        checkArgumentCustom(user.getId().equals(currentUserId()) || isAdmin(), NO_AUTHORITIES_TO_CHANGE_THIS_USER);
        checkArgumentCustom(isNotBlank(form.getEmail()) && isValidMailAddress(form.getEmail()), WRONG_FORMAT_OF_EMAIL);
        if (!form.getEmail().equalsIgnoreCase(user.getEmail())) {
            checkArgumentCustom(isEmailNotRegistered(form.getEmail()), EMAIL_ALREADY_EXISTS);
            user.setEmail(trimToNull(form.getEmail()));
        }
        if (form.getPassword() != null && form.getOldPassword() != null) {
            checkArgumentCustom(passwordEncoder.matches(form.getOldPassword(), user.getPassword()), WRONG_PASSWORD);
            checkArgumentCustom(PASSWORD_PATTERN.matcher(form.getPassword()).matches(), WRONG_FORMAT_OF_PASSWORD);
            checkArgumentCustom(form.getPassword().equals(form.getRepeatPassword()), PASSWORD_DOES_NOT_MATCH);
            user.setPassword(passwordEncoder.encode(trimToNull(form.getPassword())));
        }
        if (form.getFirstName() != null) {
            user.setFirstName(trimToNull(form.getFirstName()));
        }
        if (form.getLastName() != null) {
            user.setLastName(trimToNull(form.getLastName()));
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
