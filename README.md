# java-filmorate
Template repository for Filmorate project

```mermaid
erDiagram
Film_Genre {
    int film_id PK
    int genre_id PK
    }
    Film_Genre }o--o{ Genre : set
Genre {
    int genre_id PK
    String description
    }
    Film ||--o{ Film_Genre : set
Film {
    int film_id PK
    String name
    String description
    int duration
    Date date
    }
    Film ||--o{ Film_Respect:setFilmId
Film_Respect {
    int film_id PK
    int user_id PK
    }
    Film_Respect ||--o{ User : setUserId
    User {
    int user_id PK
    String email
    String login
    String name
    Date birthday
    }
    User ||--o{ Friends : setUserAndFriendId
    Friends {
    int user_id PK
    int friends_id PK
    }
```
![This is an image](scr/main/resources/scheme.png)