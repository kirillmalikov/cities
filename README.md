# Helmes Test Assignment

### Task statement
Create an enterprise-grade "city list" application (it will stay there for years, will be extended and
maintained) which allows the user to do the following:
 * browse through the paginated list of cities with the corresponding photos
 * search by the name
 * edit the city (both name and photo)
#### Notes
 * initial list of cities should be populated using the attached cities.csv file
 * city addition, deletion and sorting are not in the scope of this task
 * usage of Spring Data REST is prohibited

### Used technologies

* Java 17
* Spring Boot 2.7
* Postgres
* JUnit
* Mockito
* Gradle
* React 
* Typescript
* Docker

## Running the project

1. Download .zip or clone the project from GitHub.

2. Please make sure that you have Docker (Docker Desktop in case of Windows PC) installed and operational.
3. Use Docker to run the database and both the backend and frontend.
   ```sh
   docker compose up
   ```
4. It may take some time to boot everything up. After a successful build and start - the UI will be available at http://localhost:3000
5. Please allow cookies for this service to use it. 

## How to use
### NB!
The UI may offend your sense of beauty and hurt your eyes - you have been warned :wink:

1. First of all you need to register a user.
Please enter your email and password into the form on the right side of the header that will be shown on hover over an 'Account' text.
Check the 'Allow edit' checkbox if you want to have the 'edit rights'.
2. After signing up - the content (city list) would be rendered automagically.
3. You can change theme using a switch at the bottom of 'Account' drop-down block.
4. For traversing through a list of cities please use the pagination controls at the bottom of the screen.
5. Use the search bar by the center of a header to search for a city by its name.
6. For editing simply double-click on a city and the edit menu will appear.
To hide it - either make changes and save or just double-click on a city again.

## Important notes

* The main focus during this assignment was on the backend, thus the frontend code, style, design and practices may not be perfect, though I was trying to do my best.
* The UI design was inspired by a souvenir plates that are commonly bought by tourists in different cities. 
* Application uses cookie-based authentication and JWT token.
* Custom exception handling was introduced on the backend side to return meaningful information to the 'client'.
* Frontend tests and error handling on UI were not included due to the lack of time (there are pretty informative error logs in a console though).
