# Use official Node.js image
FROM node:20-alpine

# Set working directory inside container
WORKDIR /app

# Copy package.json and package-lock.json / yarn.lock
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy project files
COPY . .

# Expose Vue CLI dev server port
EXPOSE 8080

# Start Vue CLI dev server
CMD ["npm", "run", "serve"]
