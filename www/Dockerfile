FROM nginx:latest

WORKDIR /usr/share/nginx/html

EXPOSE 80

COPY ./src/index.html ./index.html
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf
