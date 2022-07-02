# java-filmorate
Template repository for Filmorate project

```mermaid
erDiagram
Film_Genre {
    int film_id PK
    int genre_id PK
    }
    
Film_Genre }o--|| Genre : set
    
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
    
Film ||--|| Film_Rating : setFilmId
    
Film_Rating {
    int film_id
    int rating_id
    }
    
    Film_Rating ||--|| RatingMPI : setRatingId
    
    
RatingMPI {
    int rating_id
    String description
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
    String confirm
    }
```
![This is an image](https://github.com/RedStarMisha/java-filmorate/tree/add-friends-likes/src/main/resources/scheme.png?raw=true)

```markdown
#получение списка общих друзей
SELECT friends_id 
FROM Friends  
WHERE user_id=1 OR user_id=3 GROUP BY friends_id 
HAVING COUNT(*) > 1; 

```
```markdown
#самый популярный фильм
SELECT MAX(c) 
FROM (
    SELECT COUNT(user_id) as c 
    FROM Film_Respect 
    GROUP By film_id)
```