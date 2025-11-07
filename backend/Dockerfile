FROM node:latest 

WORKDIR /app 

COPY . . 

RUN npm install 

RUN npm install prom-client

CMD [ "npm", "start" ]