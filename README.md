# Install docker on ec2
sudo yum update -y
sudo yum install docker -y
sudo service docker start

# push image to docker hub
docker build --no-cache --pull -t jaypbevoor/agribot .
docker run -p 8080:8080 jaypbevoor/agribot -d
docker exec -it <containerId> /bin/bash
sudo docker login
usrename: jaypbevoor
pass: docker
docker push jaypbevoor/agribot

# pull image from docker hub
sudo docker login
usrename: jaypbevoor
pass: docker
sudo docker pull jaypbevoor/agribot:1