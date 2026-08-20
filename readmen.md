# GET user by id
curl -X GET "http://localhost:8080/api/users/123"

# POST create user (raw string body)
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: text/plain" \
  --data "new-user-payload"


curl -u user:YOUR_PASSWORD -X GET "http://localhost:8080/api/users/123"
curl -u user:YOUR_PASSWORD -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: text/plain" \
  --data "new-user-payload"

terminate port 
pids=$(lsof -ti tcp:8081); if [[ -n "$pids" ]]; then kill -9 $pids; fi

## API Gateway + JWT + Microservices

### Run services

```bash
# submissions service (port 8082)
./mvnw -f microservices/submissions-service/pom.xml spring-boot:run
```

```bash
# payments service (port 8083)
./mvnw -f microservices/payments-service/pom.xml spring-boot:run
```

```bash
# gateway (port 8080)
./mvnw -f microservices/gateway/pom.xml spring-boot:run
```

### Get JWT token from gateway

```bash
curl "http://localhost:8080/auth/token?userId=alice&role=user"
```

### Use token through gateway

```bash
TOKEN="<paste-token-here>"
```

```bash
curl -X POST "http://localhost:8080/api/submissions" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"First submission"}'
```

```bash
curl -X GET "http://localhost:8080/api/submissions" \
  -H "Authorization: Bearer $TOKEN"
```

```bash
curl -X POST "http://localhost:8080/api/payments" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount":39.99,"currency":"USD"}'
```

```bash
curl -X GET "http://localhost:8080/api/payments" \
  -H "Authorization: Bearer $TOKEN"
```

TOKEN=$(curl -s "http://localhost:8080/auth/token?userId=alice&role=user" | sed -E 's/.*"token":"([^"]+)".*/\1/'); curl -i -X GET "http://localhost:8080/api/submissions" -H "Authorization: Bearer $TOKEN"
