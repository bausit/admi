# admin
This project uses Spring Data Rest to expose REST endpoints
to perform CRUD operations.

## Security
### Request tokens
`
curl --location --request POST 'http://localhost:9090/api/token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "danny@mail.com",
    "password": "password"
}'
`
### Token response
`
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5ueUBtYWlsLmNvbSIsImlhdCI6MTYwOTY0MDgzNCwiZXhwIjoxNjA5NjU4ODM0fQ.DDhy5CCLH8hxqxgHPOr15Pygro4aTKzA7dBThZmmQeIU3Bg7nsT-PWFw7qrmYBkqVAY74HYClaiGHdj-iHXGoQ",
    "type": "Bearer",
    "id": 13,
    "username": "danny@mail.com",
    "email": "danny@mail.com",
    "roles": [
        "admin"
    ]
}
`

### Request with token
`
curl http://localhost8080/json/skills \
-H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5ueUBtYWlsLmNvbSIsImlhdCI6MTYwOTY0MDgzNCwiZXhwIjoxNjA5NjU4ODM0fQ.DDhy5CCLH8hxqxgHPOr15Pygro4aTKzA7dBThZmmQeIU3Bg7nsT-PWFw7qrmYBkqVAY74HYClaiGHdj-iHXGoQ'
`

## List objects
`curl http://localhost8080/json/skills`

## Creating a new object
`
curl --location --request POST 'http://localhost:8080/json/skills' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "description": "test"
}'
`

## Overwrite objects
`
curl --location --request PUT 'http://localhost:8080/json/skills/15' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test2",
    "description": "test",
    "id": 15
}'`

## Partial update object
`
curl --location --request PATCH 'http://localhost:8080/json/skills/16' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test3"
}'
`

## Deleting objects
`curl --location --request DELETE 'http://localhost:8080/json/skills/15'`

## Update object relationship
### Add a skill to members
`
curl --location --request POST 'http://localhost:8080/json/members/7/skills' \
--header 'Content-Type: text/uri-list' \
--data-raw '1'
`
### Overwrite member skills
`
curl --location --request PUT 'http://localhost:8080/json/members/7/skills' \
--header 'Content-Type: text/uri-list' \
--data-raw '16'
`
### Remove a skill from members
`curl --location --request DELETE 'http://localhost:8080/json/members/7/skills/1'`

### Installing java in EC2
`sudo amazon-linux-extras install java-openjdk11`

### creating a function
`
curl --location --request POST 'http://localhost:9090/json/teams' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Main Shrine",
    "description": ""
}'
`

### assign function to an activity
`
curl --location --request PUT 'http://localhost:9090/json/teams/3/activity' \
--header 'Content-Type: text/uri-list' \
--data-raw '5'
`

### assign leader to a function
`
curl --location --request PUT 'http://localhost:9090/json/teams/3/leader' \
--header 'Content-Type: text/uri-list' \
--data-raw '12'
`