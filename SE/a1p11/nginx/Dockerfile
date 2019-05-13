FROM alpine:latest


MAINTAINER Chengpeng Jiang <chengpeng.jiang@mailbox.tu-dresden.de>

RUN apk update

RUN apk add nginx && mkdir -p /run/nginx

COPY ./nginx.conf /etc/nginx/
EXPOSE 80
CMD ["nginx"]