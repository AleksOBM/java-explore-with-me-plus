package ru.practicum.ewm.service;

import jakarta.transaction.Transactional;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

public interface UserService {

	@Transactional
	UserDto adminAddNewUser(NewUserRequest newUserRequest);

    void throwIfUserNotFound(Long userId);
}
