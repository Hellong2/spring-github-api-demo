
# Spring Github Api

This Spring Boot project connects to the GitHub API to retrieve some repositories data for users. It provides endpoints to fetch information about GitHub users.

## Features

- Retrieve repositories of a single GitHub user
- Simple and extensible structure for adding more GitHub API interactions

## Prerequisites

- Java 21 or higher
- Maven 3.9.7 or higher

## Getting Started

To generate the JWT token required to authenticate as a GitHub app you have to:

Sign the JWT token using the private key you configured on your GitHub app as described [here](https://docs.github.com/en/apps/creating-github-apps/authenticating-with-a-github-app/about-authentication-with-a-github-app#generating-a-private-key).  
Encode it using the RS256 algorithm.  
GitHub checks that the request is authenticated by verifying the token with the app's stored public key.  

GitHub let's you download the GitHub App private key in the PEM format which isn't natively supported by the JVM unless you leverage a third-party library such as BouncyCastle. In this guide we will convert it to DER using the openssl utility.

```bash
openssl pkcs8 -topk8 -inform PEM -outform DER -in ~/github-api-app.private-key.pem -out ~/github-api-app.private-key.der -nocrypt
```

### Clone the Repository

```bash
git clone https://github.com/Hellong2/spring-github-api-demo.git
cd spring-github-api-demo
```

### GitHub API Configuration

Need to add following properties in application.properties:


- **github.api.private.key.path** - it's a path to yor private key in .der format.
- **github.api.app.id** - appId of your github app. More info [here](https://docs.github.com/en/apps/creating-github-apps/registering-a-github-app/registering-a-github-app).
- **github.api.app.installationId** - optional, if you want to be precise which of github app installation you want to use. Defult is -1.
- **github.api.app.repos.fork.exclude** - optional, if you doesn't want to exclude all branches which are forks, then change to false. Default is true. 
- **github.api.token.expiration.date** - optional, jwt token expiration date in millis. Default is 600000.

### Api Endpoints

| HTTP Request    | Disclaimer                                                                                                |
|-----------------|-----------------------------------------------------------------------------------------------------------|
| GET /api/{user} | search for repositories of a given user (name of repo, owner login, all branches with sha of last commit) |
