# BAUS Admin
A RESTful application that’s used by frontend user interface to retrieve/update data stored in the database. It is built using Java, and Postgres.

## Building
- Install Java SDK 9
- Install Maven
- Run `mvn package`

## Running locally
`mvn spring-boot:run`

## Frameworks used
- Spring boot, creating RESTful Java application, https://spring.io/projects/spring-boot
- Spring security, ensuring only authenticated users can access the application, https://spring.io/projects/spring-security
- Spring Data, interacting with database, https://spring.io/projects/spring-data
- Spring Data Rest, exposing basic endpoints to retrieve/update data, https://spring.io/projects/spring-data-rest

## Security
With exception to token request endpoint, `/api/token`, all other requests require have to a valid JWT token in `Authorization` header.
Access for individual endpoints is configured at https://github.com/bausit/admin/blob/main/src/main/java/org/bausit/admin/configs/SecurityConfig.java


## Checkin to events
Starting the application
nohub java -jar apps/baus-admin.jar &

Starting the application auto creates an event for today
users in src/resources/sample-data.csv are not invited to the event, their checkin request will be rejected
admins: BigDog@mail.com, danny@mail.com, Long@mail.com are invited and able to check in



1. Getting today's event
```
   curl --location --request GET 'http://107.172.60.46:9090/api/events/today'
   {
   "26": "清明法会Chinese Lunar New Year Blessing Ceremony"
   }
```


2. checkin to the event, repeated checkin attempts after the first try will be ignored, but would still return session token
```
   curl --location --request POST 'http://107.172.60.46:9090/api/events/26/checkin' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "emailOrPhone": "danny@mail.com",
   "date": "2023-08-06",
   "hour": 8
   }'
```

Response
```json
{
"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW5ueUBtYWlsLmNvbSIsImlhdCI6MTY5MTIxMjI2MiwiZXhwIjoxNjkxMjMwMjYyfQ.0semiVuf8NrjcrKpBQBHBotkITsodvcQMLt44hs7thegtrFD0UDM8-0v82p10csXCx4hVzShQj5nq5Q9KY4Xbw",
"type": "Bearer",
"id": 23,
"name": "danny Admin",
"email": "danny@mail.com",
"roles": [
"super"
]
}
```

3. Fetch user profile
```
   curl --location --request GET 'http://107.172.60.46:9090/api/profile' \
   --header 'Authorization: Bearer {{user token from 2)}}'
```

Response
```json
{
"chineseName": "名字",
"firstName": "danny",
"lastName": "Admin",
"email": "danny@mail.com",
"phoneNumber": "555-123-0000",
"address": "address for danny",
"city": "New York",
"state": "NY",
"zipcode": "10011"
}
```

4. Update profile
```
   curl --location --request POST 'http://107.172.60.46:9090/api/profile' \
   --header 'Authorization: Bearer {{user token from 2)}}'
   --header 'Content-Type: application/json' \
   --data-raw '{
   "chineseName": "名字",
   "firstName": "danny",
   "lastName": "Admin",
   "email": "danny@mail.com",
   "phoneNumber": "555-123-0000",
   "address": "address for danny",
   "city": "New York",
   "state": "NY",
   "zipcode": "10011"
   }'

```

5. List checkins for an event
```
curl --location --request POST 'http://107.172.60.46:9090/api/events/26' \
   --header 'Authorization: Bearer {{admin user token)}}'
```

Response
```json
{
  "name": "清明法会Chinese Lunar New Year Blessing Ceremony",
  "location": "the Temple",
  "date": "2023-08-08T02:15:20.565263Z",
  "teams": [
   
  ],
  "checkins": [
    {
      "id": 0,
      "participant": {
        "memberNumber": 0,
        "chineseName": "名字",
        "firstName": "danny",
        "lastName": "Admin",
        "issueDate": "2023-08-08T02:15:20.386705Z",
        "type": "V",
        "email": "danny@mail.com",
        "phoneNumber": "555-123-0000",
        "birthYear": 2000,
        "birthDate": null,
        "gender": "M",
        "status": null,
        "refuge": false,
        "address": "address for danny",
        "city": "New York",
        "state": "NY",
        "zipcode": "10001",
        "emergencyContact": null,
        "skills": [
          {
            "name": "Chef",
            "description": "Chef",
            "id": 13
          },
          {
            "name": "Engineering",
            "description": "Engineering",
            "id": 14
          }
        ],
        "note": null,
        "remark": null,
        "permissions": null,
        "preferences": null,
        "id": 23,
        "englishName": "danny Admin",
        "skillsAsString": "Chef, Engineering",
        "permissionsAsString": ""
      },
      "event": null,
      "checkinDate": "2023-08-08T02:18:44.103722Z",
      "checkoutDate": "2023-08-06T12:00:00Z"
    }
  ],
  "invitedParticipants": [
    {
      "memberNumber": 0,
      "chineseName": "名字",
      "firstName": "Long",
      "lastName": "Admin",
      "issueDate": "2023-08-08T02:15:20.209352Z",
      "type": "V",
      "email": "long@mail.com",
      "phoneNumber": "555-123-0000",
      "birthYear": 2000,
      "birthDate": null,
      "gender": "M",
      "status": null,
      "refuge": false,
      "address": "address for Long",
      "city": "New York",
      "state": "NY",
      "zipcode": "10001",
      "emergencyContact": null,
      "skills": [
        {
          "name": "Chef",
          "description": "Chef",
          "id": 13
        },
        {
          "name": "Engineering",
          "description": "Engineering",
          "id": 14
        }
      ],
      "note": null,
      "remark": null,
      "permissions": null,
      "preferences": null,
      "id": 21,
      "englishName": "Long Admin",
      "skillsAsString": "Chef, Engineering",
      "permissionsAsString": ""
    }
  ],
  "id": 26
}
```

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

## Using endpoints exposed by `Spring Data Rest` 
### List objects
`curl http://localhost8080/json/skills`

### Creating a new object
`
curl --location --request POST 'http://localhost:8080/json/skills' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "test",
"description": "test"
}'
`

### Overwrite objects
`
curl --location --request PUT 'http://localhost:8080/json/skills/15' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "test2",
"description": "test",
"id": 15
}'`

### Partial update object
`
curl --location --request PATCH 'http://localhost:8080/json/skills/16' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "test3"
}'
`

### Deleting objects
`curl --location --request DELETE 'http://localhost:8080/json/skills/15'`

### Update object relationship
#### Add a skill to members
`
curl --location --request POST 'http://localhost:8080/json/members/7/skills' \
--header 'Content-Type: text/uri-list' \
--data-raw '1'
`
#### Overwrite member skills
`
curl --location --request PUT 'http://localhost:8080/json/members/7/skills' \
--header 'Content-Type: text/uri-list' \
--data-raw '16'
`
#### Remove a skill from members
`curl --location --request DELETE 'http://localhost:8080/json/members/7/skills/1'`

### creating a function
`
curl --location --request POST 'http://localhost:9090/json/teams' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Main Shrine",
"description": ""
}'
`

#### assign function to an activity
`
curl --location --request PUT 'http://localhost:9090/json/teams/3/activity' \
--header 'Content-Type: text/uri-list' \
--data-raw '5'
`

#### assign leader to a function
`
curl --location --request PUT 'http://localhost:9090/json/teams/3/leader' \
--header 'Content-Type: text/uri-list' \
--data-raw '12'
`