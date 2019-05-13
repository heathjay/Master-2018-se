FROM ubuntu:16.04
      # add more stuff here
MAINTAINER Chengpeng Jiang <chengpeng.jiang@mailbox.tu-dresden.de>

RUN apt-get update \
	&& apt-get upgrade -y

#pre_install
RUN apt-get install build-essential make wget python gcc openssh-server sshpass sudo r-base -y


#ssh_config
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd \
	&& sed -i "s/PermitEmptyPasswords no/PermitEmptyPasswords yes/g" /etc/ssh/sshd_config \
	&& sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
	&& sed -i 's/22/10022/' /etc/ssh/sshd_config
RUN mkdir -p /var/run/sshd

#addusers
RUN useradd -d /home/Ubuntu -ms /bin/bash -u 1000 -g root Ubuntu \
	&& echo 'Ubuntu:jiangcp' | chpasswd \
	
#RUN echo 'Ubuntu ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile

#test mcperf
RUN wget -O mcperf.tar.gz https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/twemperf/mcperf-0.1.1.tar.gz \
	&& mkdir mcperf && tar -xvf mcperf.tar.gz --strip-components=1  -C ./mcperf/ \
	&& cd /mcperf && ./configure && make && make install

#install dude
RUN wget -O dude.tar.gz https://bitbucket.org/db7/dude/get/v3.1.tar.gz && mkdir dude && tar -xvf dude.tar.gz --strip-components=1 -C ./dude/	\
	&& cd /dude && python setup.py install


#publickey
RUN mkdir /home/Ubuntu/.ssh
RUN echo 'ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAt2TT7c/Y/693GKH1sAKpAPu/CsrUsq1da9HcxagbUCHNKlhzDzqC5qmbEGOxD0bLRBJICt2Pe9Zx7W80ndhq67dR1ZUUWVT29T8TVqUjGK02WyAmaLg5HWlizYKwS5oucD9qcWJfXlgKIx5OkpbzzPiCuAjnWonFGGp9sADlAC1VRmLvI4NH5bKtqGILFYHRvcKt7V/5PtrWM17j4KqWY9g1RK2Yw9YlUXV8oVVyXBUZhrmhkwSEmwzOh5c/K0EhrqfonPo4W654PhkBZ9rxaUq6zgV/rmYvJdmOd5wRH1W8+oaf/voa4xEur5c6MYWOj2kPwx+JlmkmTUzdTen2gQ== André Martin' > /home/Ubuntu/.ssh/authorized_keys

#permission_nokey
RUN chmod 755 /home/Ubuntu \
	&& chmod 755 /home/Ubuntu/.ssh \
	&&  chmod 644 /home/Ubuntu/.ssh/authorized_keys

RUN echo 'Ubuntu ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

RUN echo 'jiangcp' > sshpass.txt && chmod 777 sshpass.txt


RUN mkdir -p /home/Ubuntu
WORKDIR /home/Ubuntu
COPY . /home/Ubuntu


RUN chown -R Ubuntu:root /home/Ubuntu
RUN chmod +x /home/Ubuntu/run.sh

RUN echo 'jiangcp' > /home/Ubuntu/sshpass.txt



EXPOSE 10022 

CMD ["/usr/sbin/sshd", "-D"]