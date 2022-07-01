# java-filmorate
Template repository for Filmorate project

```mermaid
erDiagram
Genre {
    int genre_id PK
    String description
    }

Film_Genre {
    int film_id PK
    int genre_id PK
    }
    Film_Genre 0}--|| Genre : set
```