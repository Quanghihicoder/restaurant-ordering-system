import express from "express";
import bodyParser from "body-parser";
import cors from "cors";
import path from "path";
import router from "./routes/routes.js";

// Prometheus client setup
import client from "prom-client";

// Initialize path for static files
const __dirname = path.resolve();

// Initialize express
const app = express();

// Prometheus setup
const register = new client.Registry();
client.collectDefaultMetrics({ register });

// Custom metrics: HTTP Request Counter and Response Time Histogram
const httpRequestCounter = new client.Counter({
  name: "http_requests_total",
  help: "Total number of HTTP requests",
  labelNames: ["method", "route", "status_code"],
});
register.registerMetric(httpRequestCounter);

const responseTimeHistogram = new client.Histogram({
  name: "http_response_time_seconds",
  help: "HTTP response duration in seconds",
  labelNames: ["method", "route"],
});
register.registerMetric(responseTimeHistogram);

// Middleware to track HTTP requests and response times
app.use((req, res, next) => {
  const end = responseTimeHistogram.startTimer();
  res.on("finish", () => {
    // Increment the request counter
    httpRequestCounter.labels(req.method, req.path, res.statusCode).inc();
    // Track response time
    end({ method: req.method, route: req.route?.path || req.path });
  });
  next();
});

// Middleware setup
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cors());

// Metrics endpoint for Prometheus scraping (Make sure this is defined early)
app.get('/metrics', async (req, res) => {
  try {
    res.set('Content-Type', register.contentType);
    res.end(await register.metrics());
  } catch (err) {
    res.status(500).send(err);
  }
});

// Routes setup (Make sure other routes are defined after /metrics)
app.use(router);

// Static files and SPA (if needed)
app.use(express.static(path.join(__dirname, './restaurant_management/')));

app.get('/*', (req, res) => {
  res.sendFile(path.join(__dirname, './restaurant_management/index.html'));
});

// API Test endpoint (optional)
app.get('/api', (req, res) => {
  res.json({ message: 'Welcome to the restaurant API' });
});

// Set up server port
const PORT = process.env.PORT || 8001;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});
