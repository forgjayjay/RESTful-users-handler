# API Documentation

## Endpoints

### _POST /api/postUser_

#### Description
Creates a new user.

#### Request Body
- JSON representation of the user object.

#### Response
- 201 Created: If the user is successfully created, returns the URI of the newly created user and the user data.
- 400 Bad Request: If the user already exists or if there are constraint violations.

### _DELETE /api/deleteUser_

#### Description
Deletes a user.

#### Request Body
- JSON representation of the user object to be deleted.

#### Response
- 200 OK: If the user is successfully deleted.
- 400 Bad Request: If the user doesn't exist.

### _DELETE /api/deleteUser/{id}_

#### Description
Deletes a user by ID.

#### Path Parameters
- `id`: The ID of the user to be deleted.

#### Response
- 200 OK: If the user is successfully deleted.
- 400 Bad Request: If the user with the specified ID doesn't exist.

### _PUT /api/putUser/{id}_

#### Description
Updates a user.

#### Path Parameters
- `id`: The ID of the user to be updated.

#### Request Body
- JSON representation of the updated user object. Can contain all or some of the fields to be updated.

#### Response
- 200 OK: If the user is successfully updated, returns the updated user data.
- 400 Bad Request: If the user with the specified ID doesn't exist or if the update contains constraint violations.

### _GET /api/getUsers_

#### Description
Retrieves all users.

#### Response
- 200 OK: Returns a JSON array containing all user data.

### _GET /api/getUsers/{id}_

#### Description
Retrieves a user by ID.

#### Path Parameters
- `id`: The ID of the user to be retrieved.

#### Response
- 200 OK: If the user is found, returns the user data.
- 400 Bad Request: If the user with the specified ID doesn't exist.

### _GET /api/getUsersByDate_

#### Description
Retrieves users within a specified date range.

#### Request Parameters
- `from`: Start date (in yyyy-MM-dd format).
- `to`: End date (in yyyy-MM-dd format). Defaults to the current date if not provided.

#### Response
- 200 OK: Returns a JSON array containing users within the specified date range.
- 400 Bad Request: If the date range is invalid.

## Error Handling
- Internal server errors are logged, and an `InternalApiException` is thrown.
- API request errors are logged, and an `ApiRequestException` is thrown.
