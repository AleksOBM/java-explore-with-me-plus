# Explore with me

![Static Badge](https://img.shields.io/badge/Java-21-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-3.5.9-green)
![Static Badge](https://img.shields.io/badge/Lombok-red)
![Static Badge](https://img.shields.io/badge/JPA--specification-8A2BE2)
![Static Badge](https://img.shields.io/badge/RestTemplate-538681)
![Static Badge](https://img.shields.io/badge/PostgreSQL-16.1-blue)
![Static Badge](https://img.shields.io/badge/docker_compose-blue)
![Static Badge](https://img.shields.io/badge/H2_database-blue)
![Static Badge](https://img.shields.io/badge/JUnit-5-orange)
![Static Badge](https://img.shields.io/badge/Mockito-green)
![Static Badge](https://img.shields.io/badge/Jacoco-red)
![Static Badge](https://img.shields.io/badge/Maven-orange)
![Static Badge](https://img.shields.io/badge/Multi--module--project-8A2BE2)


## Бэкэнд для сервиса поиска мероприятий

**Учебный проект**

<img alt="preview.png" src=".img/preview.png" width="600"/>

### Основные возможности
- Просмотр предстоящих мероприятий
- Фильтрация мероприятий по различным параметрам
- Запись на мероприятие
- Оценка мероприятия (лайк/дизлайк)
- Добавление новых мероприятий
- Одобрение/отклонение мероприятий администратором
- Групировка мероприятий по категориям 
- Создание/удаление категорий
- Учет количества просмотров по каждому мероприятию
- Топ по просмотрам для главной страницы
- Создание подборок событий
- Закрепление подборок на главной

### Состав проекта
- Основной сервис
- Сервис учета статистики

### Public API
```mermaid
mindmap
  root((Free API))
      🌐/categories
        GET /categories
        GET /categories/:catId
      🌐/compilations
        GET /compilations
        GET /compilations/:compId
      🌐/events
        GET /events
        GET /events/:eventId
```

### User API
```mermaid
%%{init: { 'mindmap': { 'maxNodeWidth': 500 } }}%%
mindmap
  root((User API))
      🌐/users/:userId/events/:eventId/likes
        DELETE /users/:userId/events/:eventId/likes
        POST /users/:userId/events/:eventId/likes
      🌐/users/:userId/events
        GET /users/:userId/events
        GET /users/:userId/events/:eventId
        PATCH /users/:userId/events/:eventId
        POST /users/:userId/events
      🌐/users/:userId/events/:eventId/requests
        GET /users/:userId/events/:eventId/requests
        PATCH /users/:userId/events/:eventId/requests
      🌐/users/:userId/requests
        GET /users/:userId/requests
        PATCH /users/:userId/requests/:requestId/cancel
        POST /users/:userId/requests
```

### Admin API
```mermaid
%%{init: { 'mindmap': { 'maxNodeWidth': 500 } }}%%
mindmap
  root((Admin API))
      🌐/admin/categories
        DELETE /admin/categories/:catId
        PATCH /admin/categories/:catId
        POST /admin/categories
      🌐/admin/compilations
        DELETE /admin/compilations/:compId
        PATCH /admin/compilations/:compId
        POST /admin/compilations
      🌐/admin/events
        GET /admin/events
        PATCH /admin/events/:eventId
      🌐/admin/users
        DELETE /admin/users/:userId
        GET /admin/users
        POST /admin/users
```

### Stat API
```mermaid
mindmap
  root((Stat API))
        GET /stats
        POST /hit
```

### Database map

<img alt="db" src=".img/db-schema.png" width="600"/>