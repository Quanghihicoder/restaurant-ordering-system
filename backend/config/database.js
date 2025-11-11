import mysql from "mysql2";

// create the connection to database

const databaseUrl =
  process.env.DATABASE_URL || "mysql://root:root@mysql:3306/qfood";

const db = mysql.createConnection(databaseUrl);

db.connect((error) => {
  if (error) throw error;
  console.log("Successfully connected to the database.");
});

export default db;
