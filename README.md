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



```