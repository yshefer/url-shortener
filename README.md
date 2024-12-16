# url-shortener

A URL Shortening service that converts long URLs into compact links.

## API endpoints

### **Shorten a URL**
- **POST** `/create`
- Request body:
  ```json
  {
    "url": "https://example.com"
  }
  ```
- Response:
  ```json
  {
    "url": "http://localhost:8080/lMKAy"
  }
  ```

### **Redirect to original URL**
- **GET** `/{shortUrlId}`
- Redirects to the original long URL associated with the short url id.

---

## Running locally

- Set the local profile active by specifying the environment variable `spring.profiles.active=local`

- Set the H2 database user and password using appropriate environment variables.

## Database configuration

### Local development

- This application uses an **H2 file-based database** for local development by default.

- You can access the H2 Console to view the database schema and data at `http://localhost:8080/h2-console`.

### Setting up PostgreSQL for Dev and Live environments

This application is configured to use PostgreSQL for development and production environments.

- Make sure PostgreSQL is installed and running.

- Replace the placeholders in application-{env}.yml with appropriate environment variables and don't forget to activate the profile.

---
