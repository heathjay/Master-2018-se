# Systems Engineering I - Assignment #2 #

In order to complete the tasks below, please fill the gaps code wise in the files given in the repository. Note: You can use any favorite editor or IDE to accomplish those tasks.

### Task ###

##### Container #1: #####
* Base image: Ubuntu 16.04 LTS
* SSH Server
* memcached v1.4.33 (build from source)
* Expose ports for SSH Server and memcached (for other container)
* Run container as user Ubuntu (id=1000) instead of root

##### Container #2: #####
* Base image: Ubuntu 16.04 LTS
* SSH Server – expose port to external port 10022
* memcached benchmark client (mcperf)
* Dude & R
* Run container as user Ubuntu (id=1000) instead of root
* Add my ssh public key in addition to yours – see below


##### Docker compose #####
* Use Docker compose to get the communication between the containers running as well as the experiment.

##### The experiment script/work flow #####
* ```dude run```:
* ssh to the memcached server (container #1) to launch memcached
* Launch the benchmark client (locally - container #2)
* Grab the output from the benchmark client using cut etc. magic: "Response rate", "Response time [ms] avg" - Dude dimensions: rate 
* ```dude sum```: summarizes the output - single csv file
* The plot the graphs ```$ Rscript ….R```

Test it using the following command sequence:
```
#!/bin/bash

sudo docker-compose up -d
ssh ubuntu@127.0.0.1 -p 10022 "./run.sh"
scp -P 10022 ubuntu@127.0.0.1:~/graph*.pdf .
evince graph*.pdf
sudo docker-compose down
```

### General Notes ###
* Solutions must be turned in no later than **11:59pm AOE, 14th of Dec‘18!** No late days or other excuses.
* Commit & PUSH!!! to your bitbucket repository before the deadline. Don't forget the push.
* No team work. We check for plagarism and will let you fail if there is an indication given.
* Ask questions at [auditorium](https://auditorium.inf.tu-dresden.de) if there are any.

My ssh-public-key:
```
ssh-rsa AAAAB3NzaC1yc2EAAAABIwAAAQEAt2TT7c/Y/693GKH1sAKpAPu/CsrUsq1da9HcxagbUCHNKlhzDzqC5qmbEGOxD0bLRBJICt2Pe9Zx7W80ndhq67dR1ZUUWVT29T8TVqUjGK02WyAmaLg5HWlizYKwS5oucD9qcWJfXlgKIx5OkpbzzPiCuAjnWonFGGp9sADlAC1VRmLvI4NH5bKtqGILFYHRvcKt7V/5PtrWM17j4KqWY9g1RK2Yw9YlUXV8oVVyXBUZhrmhkwSEmwzOh5c/K0EhrqfonPo4W654PhkBZ9rxaUq6zgV/rmYvJdmOd5wRH1W8+oaf/voa4xEur5c6MYWOj2kPwx+JlmkmTUzdTen2gQ== André Martin
```
