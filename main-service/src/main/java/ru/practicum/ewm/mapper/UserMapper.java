package ru.practicum.ewm.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.model.User;

@UtilityClass
public class UserMapper {

	public UserDto toUserDto(@NonNull User user) {
		return UserDto.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.build();
	}

	public User toUser(@NonNull UserDto userDto) {
		return User.builder()
				.name(userDto.name())
				.email(userDto.email())
				.build();
	}
}