
import sqlite3

conn = sqlite3.connect('bookstore.db')
c = conn.cursor()

c.execute('CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, title TEXT, price REAL)')
c.execute('CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY, name TEXT)')
c.execute('CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY, book_id INTEGER, customer_id INTEGER, quantity INTEGER)')

c.executemany('INSERT INTO books (title, price) VALUES (?, ?)', [
    ('Python Basics', 29.99),
    ('Flask in Action', 34.99),
    ('Machine Learning 101', 44.99),
    ('Data Science Handbook', 39.99),
    ('Algorithms Unlocked', 24.99)
])

c.executemany('INSERT INTO customers (name) VALUES (?)', [
    ('Alice',), ('Bob',), ('Charlie',)
])

c.executemany('INSERT INTO orders (book_id, customer_id, quantity) VALUES (?, ?, ?)', [
    (1, 1, 3),
    (2, 1, 2),
    (3, 2, 1),
    (1, 2, 1),
    (4, 3, 4),
    (5, 3, 2)
])

conn.commit()
conn.close()
