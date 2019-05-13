---------------------------------------------------------------
Systems Engineering 1 -- Assignment 1 --  Cold storage Modeling
---------------------------------------------------------------

--------
I. Task: 
--------
**A. Task description:**

Model using PRISM tool a file stored persistently by sharding it between 4 data centers using 3/4 coding and each shard should be stored locally in each datacenter sharded using 17/20 coding.
  
Hint-- the file is not replicated and only one copy exists.

Hint-- the file is durable if maximum one data center fails --> (failedDC<=1)

**B. Model inputs:**

Datacenter: 
- fails at rate of dcFR = 0.01% 
- repairs at rate of dcRR.

Disk: 
- fails at rate of diskFR = 5%.
- repairs at rate of diskRR.

Time frame = 1 year

**C. Task requirement:**

Calculate the file availability i.e., the probability that the file will survive one year, if the repair rates for disks and dc are respectively:
* 1 week, 1 week
* 1 week, 1 day
* 1 day, 1 week
* 1 day, 1 day.




------------------------
II. Required Files List:
------------------------
+ model.prism  >  the ctmc model code
+ props.csl    >  the property to calculate the availability 
+ ctmc.pdf     >  the ctmc state diagram
+ 1d1d.txt     >  availability when diskRR=1 day and dcRR=1 day
+ 1d1w.txt     >  availability when diskRR=1 day and dcRR=1 week
+ 1w1d.txt     >  availability when diskRR=1 week and dcRR=1 day
+ 1w1w.txt     >  availability when diskRR=1 week and dcRR=1 week

----------------------
III. Reference Slides:
----------------------
https://tu-dresden.de/ing/informatik/sya/se/ressourcen/dateien/teaching/courses/ws/systems_engineering_1/winter-semester-2018/exercises/SE1-2018-E5-CSA.pdf?lang=en

------------------
IV. Prerequisites:
------------------
* windows/linux/Mac
* Java7
* c/c++ compiler 

----------------------
V. PRISM Installation:
----------------------
http://www.prismmodelchecker.org/manual/InstallingPRISM/Instructions

----------------------------
VI. Submission Instructions:
----------------------------
- Use the empty files provided in the repository to include you solution--> do not change the names of files.
- **Deadline**: 09.12.2018--no submission will be accepted after that.

---------------
VII. Questions:
---------------
SE1 auditorium: https://auditorium.inf.tu-dresden.de/en/groups/110631001

