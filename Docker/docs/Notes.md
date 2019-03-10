#What is docker
Docker is a suite of tools used to build / deploy / run applications through containers.
Containers allow a developer to package up an application with all of the part it needs such as libraries and other dependencies, and ship it out as one package

##What is a docker container
A docker *container* is an instance of a Docker *image* and runs in its own independent environment from other containers

##What is a docker image
A docker image is a file, containing snapshot of the application and its dependencies.

##Docker commands

|Command                     | Description|
|--------------------------------- | -------|
|`docker`                    | references docker client|
|`docker run <image-name>`   | Effectively a two stop operation invoving `docker create <image-name>` followed by `docker run <container-address>`|
|`docker ps` | Displays all running containers |
|`docker ps --all` | Dispaly all containers (both running and stopped) |
|`docker system prune` | Deletes all unused containers, network, images |
|`docker exec <container-id> <cmd>` | executes a command in a docker container |
|`docker exec -it <container-id> sh` | starts a shell and attaches to STDIN and displays STDOUT and STDERR |
