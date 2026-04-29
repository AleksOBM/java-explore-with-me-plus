package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dao.UserRepository;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.util.error.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserDto adminAddNewUser(NewUserRequest newUserRequest) {
		User user = UserMapper.toEntity(newUserRequest);
		return UserMapper.toUserDto(userRepository.save(user));
	}

	@Override
	public List<UserDto> getUsers(List<Long> ids, int from, int size) {
		PageRequest page = PageRequest.of(from / size, size);
		List<User> users;
		if (ids == null || ids.isEmpty()) {
			users = userRepository.findAll(page).getContent();
		} else {
			users = userRepository.findAllByIdIn(ids, page);
		}
		return users.stream()
				.map(UserMapper::toUserDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new NotFoundException("User with id=" + userId + " not found");
		}
		userRepository.deleteById(userId);
	}
}
