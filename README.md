# com.crud.json
 A simple JSON CRUD application (Lombok,Spring Data, h2, HATEOAS) with implemented support for HATEOAS.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Java SE JDK 1.8,

GRADLE 5.6

### Installing (Windows)

1. Download and extract repository

2. Open command line at extracted directory

3. Type:
```
gradle build
```
4. Gradle will generate jar at ../com.crud.json/build/libs/

### CRUD URLs (JSON)

http:\\localhost:8080\employees (get) - returns paged Employee list wrapped into Resources

http:\\localhost:8080\employee\{id} (get) - returns Employee object wrapped into Resource

http:\\localhost:8080\employees (post) - accepts plain JSON object with fields (name, surname, email) 

http:\\localhost:8080\employee\{id} (put)

http:\\localhost:8080\employee\{id} (delete)

Validation:

email: hibernate email validation

name & surname : not blank/null, max 40 characters

### Testing

To be implemented

## License

This project is licensed under the [Apache License 2](https://www.apache.org/licenses/LICENSE-2.0)
