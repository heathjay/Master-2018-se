FROM ubuntu:16.04

#	maintainer
MAINTAINER Chengpeng Jiang <chengpeng.jiang@mailbox.tu-dresden.de>

RUN apt-get update && apt-get upgrade -y
# add more stuff here

#pre_installbuild-essential
RUN apt-get install build-essential make wget python gcc sshpass openssh-server sudo -y

RUN mkdir -p /var/run/sshd


#ssh_config
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
	&& sed -i "s/PermitEmptyPasswords no/PermitEmptyPasswords yes/g" /etc/ssh/sshd_config \
	&& sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
	&& sed -i 's/22/7777/' /etc/ssh/sshd_config


#addusers
RUN useradd -d /home/Ubuntu -ms /bin/bash -u 1000 -g root Ubuntu \
	&& echo 'Ubuntu:jiangcp' | chpasswd \
#	&& usermod -aG sudo Ubuntu 
	
#RUN echo 'Ubuntu ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers


ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

#wget libevent
RUN wget -P / https://github.com/libevent/libevent/releases/download/release-2.1.8-stable/libevent-2.1.8-stable.tar.gz
RUN tar -xzf libevent-2.1.8-stable.tar.gz
RUN cd libevent-2.1.8-stable && ./configure  && make && make install 
RUN cd ..


#wget memcached v1.4.33
RUN wget -P / http://www.memcached.org/files/memcached-1.4.33.tar.gz 
RUN tar -xzf memcached-1.4.33.tar.gz 
RUN cd memcached-1.4.33 && ./configure && make && make install
RUN ln -s /usr/local/lib/libevent-2.1.so.6 /usr/lib/libevent-2.1.so.6
RUN cd ..



EXPOSE 7777 11211

CMD ["/usr/sbin/sshd", "-D"]

