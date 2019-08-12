# Withergate API

This is a resource server used for the Withergate project. The application is written in Spring Boot and expects an [authorization server](https://github.com/Withergate/auth) to be running (the endpoint is specified in the appropriate `application.properties`) configuration file.

## 🔧 Installation

This is a [Gradle](https://gradle.org/) project and uses [lombok](https://projectlombok.org/), which needs to be configured in any IDE.

## 🚦 Usage

The application can be run using the included Gradle wrapper: `./gradlew bootRun`

Similarly, building the application can be run using `./gradlew clean build`.

The application can be run in two profiles:

- local - contains in-memory database for testing and local startup
- live - used for cloud deployment to GCP, needs to have GOOGLE_APPLICATION_CREDENTIALS and database password injected in order to work

When testing the full stack, it is highly recommended to run all the game components on the local machine. This can be easily done using the bundled `docker-compose` by running `docker-compose up`.

The game will be then accessible at `http://localhost:3000`.

### 🤝 Authentication

Each request to this server must contain a `Bearer` token retrieved from the authorization server in the `Authorization` header.

## 🔖 License

The code is released under the Apache 2.0 license. See [LICENSE](https://github.com/Withergate/api/blob/master/LICENSE) for details.
