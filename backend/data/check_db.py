import sqlite3
conn = sqlite3.connect('gaokao.db')
cur = conn.cursor()

# Show university table schema and sample data
cur.execute("PRAGMA table_info(university)")
print("=== university columns ===")
for r in cur.fetchall():
    print(" ", r)

cur.execute("SELECT * FROM university LIMIT 5")
print("\n=== university sample ===")
for r in cur.fetchall():
    print(" ", r)

# Show major table schema
cur.execute("PRAGMA table_info(major)")
print("\n=== major columns ===")
for r in cur.fetchall():
    print(" ", r)

cur.execute("SELECT * FROM major LIMIT 5")
print("\n=== major sample ===")
for r in cur.fetchall():
    print(" ", r)

conn.close()
