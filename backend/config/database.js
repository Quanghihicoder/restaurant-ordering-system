import mysql from "mysql2";

// create the connection to database

const db = mysql.createConnection({
  host: process.env.MYSQL_HOST || "mysql",
  user: process.env.MYSQL_USER || "root",
  password: process.env.MYSQL_PASSWORD || "root",
  database: process.env.MYSQL_DATABASE || "db_restaurant"
});

db.connect(error => {
  if (error) throw error;
  console.log("Successfully connected to the database.");
});

export default db;