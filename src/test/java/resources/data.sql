merge into RATING using values ( 1, 'G' ), (2, 'PG'), (3, 'PG-13'), (4, 'R'),(5, 'NC-17') d(k,v)
    on (RATING_ID=d.k) when not matched then insert values (d.k, d.v);

merge into GENRE using values ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик') d(v)
    on (GENRE_NAME=d.v) when not matched then insert (GENRE_NAME) values (d.v);
