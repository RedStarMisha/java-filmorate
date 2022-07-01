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
    int film_id
    String name
    String description
    int duration
    Date date
    }


```