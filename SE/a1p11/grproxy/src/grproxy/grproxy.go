package main

import (
	"fmt"
	"github.com/samuel/go-zookeeper/zk"
	"log"
	"math/rand"
	"net/http"
	"net/http/httputil"
	"time"
)


func main() {
	time.Sleep(30*time.Second)
//	startServer()
	var urls []string
	conn := ZookeeperProcess()
	TempSnapshots, errors := Monitor(conn, "/grproxy")
	go func() {
		for {

			select {
				case snapshots := <-TempSnapshots:
					fmt.Printf("%+v :good\n", snapshots)
					var temp []string
					for _, snapshot := range snapshots {
						gserve_urls, _, err := conn.Get("/grproxy/" + snapshot)
						temp = append(temp, string(gserve_urls))
						if err != nil {
							fmt.Printf("from zookeeper process snapshot: %+v\n", err)
						}
						fmt.Printf(string(gserve_urls))
					}

					urls = temp
					fmt.Printf("urls success:%+v \n", urls)
				case err := <-errors:
					fmt.Printf("zookeeper process-monitor TempSnapshots error:%+v \n", err)
			}
		}
	}()

	ReverseProxy := NewMultipleHostReverseProxy(urls)
	log.Fatal(http.ListenAndServe(":9999", ReverseProxy))
}


func startServer(){
	
	// ReverseProxy := NewMultipleHostReverseProxy(urls)
	// log.Fatal(http.ListenAndServe(":9999", ReverseProxy))
}


func ZookeeperProcess() (conn *zk.Conn){
	//zk connection
	conn = connect()
	defer conn.Close()	
	//grproxy register

	RegisterGrproxy(conn)
	fmt.Printf("grproxy registertion has been down")
	//gserver urls
	
	return conn
}


//zk connection
func connect() *zk.Conn {
	conn, session, err := zk.Connect([]string{"zookeeper:2181"}, 10*time.Second)
	if err != nil {
	 	fmt.Printf("conn created error")
	}

	for event := range session{
		if event.State == zk.StateConnected{
			fmt.Printf("zookeeper state connected :%s\n",event.State)
			break
		}

	}
	return conn
}

//RegisterGrproxy
func RegisterGrproxy(conn *zk.Conn){
	fmt.Printf(" gproxy connect well ...\n")

	exists, stat, err := conn.Exists("/grproxy")
	if err !=nil {
		fmt.Printf("grproxy check: %+v\n", err)
	}
	fmt.Printf("exists: %+v %+v\n", exists, stat)
	if !exists {
		grproxy, err := conn.Create("/grproxy", []byte("grproxy:80"), int32(0), zk.WorldACL(zk.PermAll))
		if err !=nil {
			fmt.Printf("grproxy root created error: %+v\n", err)
		}
		fmt.Printf("/grprocy create: %+v\n", grproxy)
	}

}



func Monitor(conn *zk.Conn, path string) (chan []string, chan error) {
    snapshots := make(chan []string)
    errors := make(chan error)

    go func() {
       for {
           snapshot, _, events, err := conn.ChildrenW(path)

           if err != nil {
               errors <- err
				fmt.Printf("childrenw: %+v\n", err)
               return
           }
           snapshots <- snapshot
           evt := <-events
           if evt.Err != nil {
				errors <- evt.Err
				fmt.Printf("monitor evt channel: %+v\n", err)
				return
			}
       }
    }()

    return snapshots, errors
}



func NewMultipleHostReverseProxy(targets []string) *httputil.ReverseProxy {
	director := func(req *http.Request) {
		HttpScheme := "http"

		//if index >= len(targets){
		//	index = 0
		//}else{
		//	index ++
		//}
		//redirct to gserver\
		fmt.Printf("length target is "+string(len(targets)))
		if req.URL.Path == "/library" {
			target := targets[rand.Int()%len(targets)]
			req.URL.Scheme = HttpScheme
			req.URL.Host = target

		}else{
			req.URL.Scheme = HttpScheme
			req.URL.Host = "nginx"

		}
	}

	return &httputil.ReverseProxy{Director: director}
}
