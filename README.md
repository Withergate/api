# Withergate API

This is a resource server used for the Withergate game. The application is written in Spring Boot and expects an [authorization server](https://github.com/Withergate/auth) to be running (the enpoint is specified in the appropriate `application.properties`) configuration file.

## 🔧 Installation

This is a [Gradle](https://gradle.org/) project and uses [lombok](https://projectlombok.org/), which needs to be configured in any IDE.

## 🚦 Usage

The application can be run using the included Gradle wrapper: `./gradlew bootRun`

Similarly, building the application can be run using `./gradlew clean build`.

The application can be run in two profiles:

- default (no profile filled): containes in-memory database for testing and local startup
- live - used for cloud deployment to GCP, needs to have GOOGLE_APPLICATION_CREDENTIALS and database password injected in order to work

### 🤝 Authentication

Each request to this server must contain a `Bearer` token retrieved from the authorization server in the `Authorization` header.

## 🔖 License

The code is released under the Apache 2.0 license. See [LICENSE](https://github.com/gigsterous/auth-server/blob/master/LICENSE) for details.
