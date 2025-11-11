import express from "express";
import bodyParser from "body-parser";
import cors from "cors";
import router from "./routes/routes.js";
import dotenv from "dotenv";
dotenv.config();

const allowedOrigins = process.env.ALLOW_ORIGIN?.split(",") || [];

// Initialize express
const app = express();

// CORS
const corsOptions = {
  origin: (origin, callback) => {
    if (!origin || allowedOrigins.includes(origin)) {
      callback(null, true);
    } else {
      callback(null, false);
    }
  },
  credentials: true,
};

app.use(cors(corsOptions));

// Middleware setup
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const sendError = (req, res) => {
  res.status(404);

  if (req.accepts("html")) {
    res.set("Content-Type", "text/html");
    res.send(`
      <!doctype html>
      <html lang="en">
      <head>
        <meta charset="utf-8">
        <title>Not Found</title>
        <meta name="description" content="Page not found">
      </head>
      <body>
        <p>Not Found! Please check your URL.</p>
      </body>
      </html>
    `);
    return;
  }

  if (req.accepts("json")) {
    res.json({ status: 0, message: "API not found!", data: [] });
    return;
  }

  res.type("txt").send("Not Found");
};

// Routes setup (Make sure other routes are defined after /metrics)
app.use(router);

// API Test endpoint (for load balancer)
app.get("/", (req, res) => {
  res.status(200).send("Welcome to the restaurant API");
});

app.use((req, res) => {
  sendError(req, res);
});

// Set up server port
try {
  const PORT = process.env.PORT || 8000;
  app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}.`);
  });
} catch {
  console.error("❌ Failed to run server:", err);
  process.exit(1);
}
