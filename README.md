# quiz-engine

### Register `POST: api/register`:
Request body:
```json
{
   "email": "nam123@gmail.com",
   "password": 12345678
}
```
Response: `200 OK`

### Add quiz `POST: api/quizzes`
Request body:
```json
{
  "title": "The Java Logo",
  "text": "What is depicted on the Java lo2222222go?",
  "options": ["Robot","Tea leaf","Cup of coffee","Bug"],
  "answer": [2]
}
```
Response: `200 OK`

### Get quiz `GET: api/quizzes/{id}`
GET ```api/quizzes/0```

Response:
```json
{
    "id": 0,
    "title": "The Java Logo",
    "text": "What is depicted on the Java lo2222222go?",
    "options": [
        "Robot",
        "Tea leaf",
        "Cup of coffee",
        "Bug"
    ]
}
```

### Get all quizzes 
Request sample: `GET: api/quizzes?page=0&size=10&sortedBy=id`

Response: 
```json
{
    "totalPages": 1,
    "totalElements": 1,
    "first": true,
    "last": true,
    "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
    },
    "number": 0,
    "numberOfElements": 1,
    "size": 10,
    "empty": false,
    "pageable": {
        "sort": {
            "sorted": true,
            "unsorted": false,
            "empty": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "content": [
        {
            "id": 0,
            "title": "The Java Logo",
            "text": "What is depicted on the Java lo2222222go?",
            "options": [
                "Robot",
                "Tea leaf",
                "Cup of coffee",
                "Bug"
            ]
        }
    ]
}
```

### Solve quiz `POST: api/quizzes/{quizId}/solve`
Request sample: `api/quizzes/0/solve`

Request body: `{answer: [2]}`

Response:
```json
{
    "success": true,
    "feedback": "Congratulations, you're right!"
}
```

### Delete quiz `DELETE: api/quizzes/{quizId}`
Request sample: `api/quizzes/0`

Response: `204 No Content`

### Get quiz completion by user `GET: api/quizzes/completed`
Response:
```json
{
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "first": true,
    "empty": false,
    "content": [
        {
            "completedAt": "2021-09-17T17:51:26.244079",
            "id": 0
        }
    ]
```


