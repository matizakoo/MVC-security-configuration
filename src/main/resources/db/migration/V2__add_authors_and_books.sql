-- Create the authors table
CREATE TABLE author (
                    id serial NOT NULL PRIMARY KEY,
                    surname VARCHAR(255) NOT NULL
);

-- Insert sample authors data
INSERT INTO author (id, surname) VALUES
                                        (1, 'Zak'),
                                        (2, 'Zaczur'),
                                        (3, 'Zakowski');

-- Create the books table
CREATE TABLE book (
                    id serial NOT NULL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    author_id INT NOT NULL
);

-- Insert sample books data
INSERT INTO book (id, name, author_id) VALUES
                                                -- Books for author 1
                                                (1, 'Book 1 by Zak', 1),
                                                (2, 'Book 2 by Zak', 1),
                                                -- Books for author 2
                                                (3, 'Book 1 by Zaczur', 2),
                                                (4, 'Book 2 by Zaczur', 2),
                                                (5, 'Book 3 by Zaczur', 2),
                                                (6, 'Book 4 by Zaczur', 2),
                                                -- Books for author 3
                                                (7, 'Book 1 by Zakowski', 3),
                                                (8, 'Book 2 by Zakowski', 3);