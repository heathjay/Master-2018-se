import os
import stat

def prepare_exp(SSHHost, SSHPort, SSHUser, optpt):
    f = open("config", 'w')
    f.write("Host server\n")
    f.write("   Hostname %s\n" % SSHHost)
    f.write("   Port %d\n" % SSHPort)
    f.write("   User %s\n" % SSHUser)
    f.close()
    

    f = open("run-experiment.sh", 'w')
    f.write("#!/bin/bash\n")
    f.write("set -x\n\n")
    
    # adjust this line to properly start memcached
    f.write("sshpass -p \"jiangcp\" ssh -F config server -o StrictHostKeyChecking=no -p 7777 \"memcached -u Ubuntu -p 11211 -P memcached.pid > memcached.out 2> memcached.err &\"\n")
    f.write("RESULT=`sshpass -p \"jiangcp\" ssh -F config server -o StrictHostKeyChecking=no -p 7777 \"pidof memcached\"`\n")
    
    f.write("sleep 5\n")

    f.write("if [[ -z \"${RESULT// }\" ]]; then echo \"memcached process not running\"; CODE=1; else CODE=0; fi\n")
        
    f.write("mcperf --linger=0 --timeout=200 --num-conns=%d   --server=%s --port=11211 2> stats.log\n\n" % ( optpt["noRequests"],  SSHHost))
    # add a few lines to extract the "Response rate" and "Response time \[ms\]: av and store them in $REQPERSEC and $LATENCY"
    f.write("REQPERSEC=$(cat stats.log | grep \"Response rate\" | awk '{print $3}')\n")
    f.write("LATENCY=$(cat stats.log | grep \"Response time \[ms\]: avg\" | awk '{print $5}')\n")
    f.write("echo \"${REQPERSEC} ${LATENCY}\"\n")
    f.write("ssh -F config benchmark \"sudo kill -9 $(cat memcached.pid)\"\n")

    f.write("echo \"response latency\" > stats.csv\n")
    f.write("echo \"$REQPERSEC $LATENCY\" >> stats.csv\n")

    f.write("if [[ $(wc -l <stats.csv) -le 1 ]]; then CODE=1; fi\n\n")
    
    f.write("exit $CODE\n")

    f.close()
    
    os.chmod("run-experiment.sh", stat.S_IRWXU) 
