package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.UserRepository;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.util.error.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserDto adminAddNewUser(NewUserRequest newUserRequest) {
		User user = UserMapper.toEntity(newUserRequest);
		return UserMapper.toUserDto(userRepository.save(user));
	}

	public void throwIfUserNotFound(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new NotFoundException("User with id " + userId + " not found");
		}
	}
}
