GOPATH := ${PWD}:${GOPATH}
export GOPATH
SET CGO_ENABLED=0
SET GOOS=linux
SET GOARCH=amd64

default: build

build:
	go build -tags netgo -v -o ./bin/gserve ./src/gserve