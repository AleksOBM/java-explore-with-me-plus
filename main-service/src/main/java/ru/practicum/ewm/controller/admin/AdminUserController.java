package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto addNewUser(@RequestBody @Valid NewUserRequest newUserRequest) {
		return userService.adminAddNewUser(newUserRequest);
	}

	@GetMapping
	public List<UserDto> getUsers(
			@RequestParam(required = false) List<Long> ids,
			@RequestParam(defaultValue = "0") int from,
			@RequestParam(defaultValue = "10") int size
	) {
		return userService.getUsers(ids, from, size);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}
}
