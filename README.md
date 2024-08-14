# Caju Authorization - Code Challenge

### Please read my development notes [here](developer-diary.md).

### Dependencies
- [Docker](https://www.docker.com/)
- [docker-compose](https://docs.docker.com/compose/)
- [OpenJDK 17](https://openjdk.org/projects/jdk/17/)

### Running the application

#### Creating your .env file
- Create one .env file on project root directory
- Define the variables inside the file

Example:

`/caju-authorization/.env`
```
DB_USERNAME=postgres
DB_PASSWORD=mysecretpassword
DB_NAME=cajudb
LOCAL_PORT=8081
DOCKER_PORT=8080
LOCAL_POSTGRESQL_PORT=5431
DOCKER_POSTGRESQL_PORT=5432
```

#### Run the following commands
- `./mvnw clean install -DskipTests`
- `./mvnw clean package -DskipTests`
- `docker-compose up --build`

#### Check if the application is running
Access http://localhost:8080/actuator/health and you can see if `status` is `"UP"`

I recommend [Insomnia](https://docs.insomnia.rest/) client, so you can import the collection [insomnia-requests.json](insomnia-requests.json) and start testing the application with a bunch of pre-made requests.

(Remember to change to docker-compose environment on Insomnia "Manage Environments" option.)
