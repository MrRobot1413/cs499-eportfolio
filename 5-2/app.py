
from flask import Flask, jsonify, render_template, send_file
import sqlite3
import matplotlib.pyplot as plt
import os

base_dir = os.path.dirname(os.path.abspath(__file__))

app = Flask(__name__)

def get_db_connection():
    conn = sqlite3.connect('bookstore.db')
    conn.row_factory = sqlite3.Row
    return conn

@app.route('/')
def home():
    return '<h2>Bookstore Reports</h2><ul>' \
           '<li><a href="/top-books">Top 5 Selling Books</a></li>' \
           '<li><a href="/top-customers">Top Customers by Revenue</a></li>' \
           '<li><a href="/chart">Sales Chart</a></li></ul>'

@app.route('/top-books')
def top_books():
    conn = get_db_connection()
    data = conn.execute('''
        SELECT books.title, SUM(orders.quantity) as total_sold
        FROM orders
        JOIN books ON orders.book_id = books.id
        GROUP BY books.title
        ORDER BY total_sold DESC
        LIMIT 5;
    ''').fetchall()
    conn.close()
    return render_template('table.html', title='Top 5 Selling Books', rows=data, headers=['Title', 'Total Sold'])

@app.route('/top-customers')
def top_customers():
    conn = get_db_connection()
    data = conn.execute('''
        SELECT customers.name, SUM(orders.quantity * books.price) AS total_spent
        FROM orders
        JOIN customers ON orders.customer_id = customers.id
        JOIN books ON orders.book_id = books.id
        GROUP BY customers.name
        ORDER BY total_spent DESC;
    ''').fetchall()
    conn.close()
    return render_template('table.html', title='Top Customers by Revenue', rows=data, headers=['Customer', 'Total Spent'])

@app.route('/chart')
def chart():
    conn = get_db_connection()
    data = conn.execute('''
        SELECT books.title, SUM(orders.quantity) as total_sold
        FROM orders
        JOIN books ON orders.book_id = books.id
        GROUP BY books.title;
    ''').fetchall()
    conn.close()

    titles = [row['title'] for row in data]
    sold = [row['total_sold'] for row in data]

    plt.bar(titles, sold)
    plt.xticks(rotation=45, ha='right')
    plt.title('Sales by Book')
    plt.tight_layout()
    chart_path = 'static/sales_chart.png'
    plt.savefig(os.path.join(base_dir, chart_path))
    plt.close()
    return f'<h3>Sales Chart</h3><img src="/{chart_path}" style="width:600px;">'

if __name__ == '__main__':
    app.run(debug=True)
