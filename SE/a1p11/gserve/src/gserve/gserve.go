package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/samuel/go-zookeeper/zk"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"time"
)

func main() {
	time.Sleep(40*time.Second)
	GserverName := os.Getenv("servername")
	startGserver(GserverName)
}


func startGserver(GserverName string){
	GserverZookeeperProcess(GserverName)
	http.HandleFunc("/library", handler)
	log.Fatal(http.ListenAndServe(":9091", nil))

}


func GserverZookeeperProcess(GserverName string) {
	//zk connection
	conn := connect(GserverName)
	defer conn.Close()
	//gserver register
	RegisterGserver(GserverName,conn)
}

//zk connection
func connect(GserverName string) *zk.Conn {
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



func RegisterGserver(GserverName string,conn *zk.Conn){
    //grproxy existed
    exists, _, _ := conn.Exists("/grproxy")
    for {
		exists, _, _ = conn.Exists("/grproxy")
    	time.Sleep(30)

    	if exists {
        	break
    		}
		fmt.Printf("%s :directory is going to be createrd.",GserverName)
	}

    fmt.Printf("/grproxy has been createrd.")

	//create gproxy/gserver
	gserv, err := conn.Create("/grproxy/"+GserverName, []byte(GserverName+":9091"), int32(zk.FlagEphemeral), zk.WorldACL(zk.PermAll))
	if err !=nil {
		fmt.Printf(GserverName+":/gserver created error losing /grpoxy/: %+v\n", err)
	}
	fmt.Printf("successfull: %+v\n", gserv)
}



func handler(w http.ResponseWriter, r *http.Request) {
	if "POST" == r.Method || "PUT" == r.Method{
		InputData,err := ioutil.ReadAll(r.Body)
		if err != nil{
			fmt.Printf("HttpBody get error")
		}

		//encode
		encodedJSONWord := encoding(InputData)
		fmt.Printf("encode successfull:" + string(encodedJSONWord))	
		postToHbase(encodedJSONWord)
		fmt.Fprintf(w, "POST successfull")
	} else if "GET" == r.Method{
		OutputData := getFromHbase()
		fmt.Fprintf(w, "Output:\n\n %s\n", string(OutputData))
	} else{
		fmt.Printf("http hanlde error")
	}

}

//Http-->Hbase Post
func postToHbase(encodedJSONWord []byte){
	ReqUrl := "http://hbase:8080/se2:library/fakerow"
//	body :=  bytes.NewBuffer([]byte(encodedJSONWord))
	body :=  bytes.NewBuffer(encodedJSONWord)
	req, err := http.Post(ReqUrl, "application/json", body)
	if err != nil {
		fmt.Println("POST INTO HBASE Error : %+v", err)
		return
	}
	fmt.Println("Post INTO HBASE SUCCESS ")
	defer req.Body.Close()
}
//Hbase-->http--->Gserver GET

func getFromHbase() string{
	//new http-->hbase 
	client := &http.Client{}
	ReqUrl := "http://hbase:8080/se2:library/*"
	req, _ := http.NewRequest("GET", ReqUrl, nil)
	req.Header.Set("Accept", "application/json")

	//get response
	Res, err := client.Do(req)
	if err != nil {
		fmt.Println("GET Request Error")
	}
	fmt.Println("Get Response: ", Res.Status)
	encodedJSONWord, err := ioutil.ReadAll(Res.Body)
	if err != nil {
		fmt.Println("Response body read fault")
	}
	//decode json
	OutputData := decoding(encodedJSONWord)
	defer Res.Body.Close()
	return OutputData
}

func encoding(InputData []byte) []byte{
	// unencoded JSON bytes from landing page
	var unencodedRows RowsType
	json.Unmarshal(InputData, &unencodedRows)

	// encode fields in Go objects
	encodedRows := unencodedRows.encode()

 	// convert encoded Go objects to JSON
   	encodedJSONWord, _ := json.Marshal(encodedRows)
	return encodedJSONWord
}

func decoding(encodedJSONWord []byte) string{

	var encodedRows EncRowsType
	json.Unmarshal(encodedJSONWord, &encodedRows)

	//  decode all fields value of go object , return RowsType
	decodedRows, err := encodedRows.decode()
	if err != nil {
		fmt.Println("decode error:%+v", err)
	}
	// convert to json byte[] from go object (RowsType)
	deCodedJSON, _ := json.Marshal(decodedRows)

	//fmt.Println("From decoder method: ", string(deCodedJSON))
	return string(deCodedJSON)
}

