# admin
This project uses Spring Data Rest to expose REST endpoints
to perform CRUD operations.

## List object types
`curl http://localhost8080/json`

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
