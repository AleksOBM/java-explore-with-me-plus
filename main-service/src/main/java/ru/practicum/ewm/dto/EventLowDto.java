package ru.practicum.ewm.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventLowDto(
		Long id,
		String annotation,
		long category,
		String description,
		LocalDateTime eventDate,
		EventLocationDto location,
		boolean paid,
		int participantLimit,
		boolean requestModeration,
		String title
) {
}

/*
{
	"annotation":"Fuga earum et sunt ratione necessitatibus dolorum sed.
				  Quae et qui porro sequi. Molestiae laboriosam nam aut beatae ea corporis. Et odio aut.",
	"category":2,
	"description":"Cum tenetur est eum facilis. Aut dolores laudantium vel sequi numquam.
					Est ut non asperiores a hic in distinctio. Sit excepturi omnis quia ipsam.
					Est nihil voluptatem voluptas.\n \rAssumenda omnis quis numquam sint aut.
					Dolorem dolores quidem impedit. Ut et voluptates necessitatibus ab.\n \r
					Sunt ad odit eveniet. Qui sapiente quos quisquam quas unde quisquam et.
					Culpa sunt possimus quis. Id aut et omnis totam enim atque facere provident et.
					Numquam quos adipisci possimus sunt.",
	"eventDate":"2026-04-26 18:21:01",
	"location":{"lat":88.9338,"lon":-73.2201},
	"paid":"true",
	"participantLimit":"8",
	"requestModeration":"false",
	"title":"Incidunt commodi est aut possimus voluptatibus."
}
 */