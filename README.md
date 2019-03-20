kwikker is a REST service for short message posting and following other users' posts.

Compilation:
mvn package

Running (Spring Boot):
mvn spring-boot:run
open http://localhost:8080

API:
1. POST "/" {"author": "username", "message": "short message (up to 140 characters)" - posts a message
2. GET "/wall/{user}" - retrieves all posts submitted by 'user', sorted in reverse chronological order
3. PUT "/follow/{follower}/{author}" - subscribes the 'follower' user to posts submitted by 'author', returns the current followed authors list
4. DELETE "/follow/{follower}/{author}" -- unsubscribes the 'follower' user from posts submitted by 'author', returns the current followed authors list