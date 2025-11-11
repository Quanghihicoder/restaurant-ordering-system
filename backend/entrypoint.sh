#!/bin/sh

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
while ! nc -z mysql 3306; do
  sleep 1
done

echo "MySQL is ready! Running migrations..."

echo "Starting backend server..."

# Run the main command (from CMD in Dockerfile or command in compose)
exec "$@"