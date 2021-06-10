# Flights R Us API Documentation

## Heroku URL
`https://cst438w3a-airline-service.herokuapp.com`

## Endpoints

<br/>

### Get All Flights
`/api/getAllFlights`

`Type: GET`

#### Description

This endpoint will return a json response with all the flights available. 

#### Parameters

    None

#### Request Example

`/api/getAllFlights`

#### Response Example

```
[
    {
        "flightId": 1,
        "airlineName": "jet-blue",
        "departureDate": "2021-07-12",
        "departureTime": "11:22:29",
        "numberOfStops": 2,
        "originCity": "seattle",
        "destinationCity": "san diego",
        "price": 200
    },
    {
        "flightId": 2,
        "airlineName": "delta",
        "departureDate": "2021-11-05",
        "departureTime": "15:14:56",
        "numberOfStops": 1,
        "originCity": "san francisco",
        "destinationCity": "boston",
        "price": 550
    },
    {
        "flightId": 3,
        "airlineName": "united",
        "departureDate": "2021-08-13",
        "departureTime": "17:23:00",
        "numberOfStops": 1,
        "originCity": "new york",
        "destinationCity": "boston",
        "price": 150
    }
]
```

<br/>

### Get Flight Dates
`/api/getFlightDates`

`Type: GET`

#### Description

This endpoint will return a json response with all the available departure dates for a specific route. 

#### Parameters

    - originCity (required) string
    - destinationCity (required) string

##### Request Example

`/api/getFlightDates?originCity=boston&destinationCity=san%20francisco`

#### Response

A successful response will return a json response with all the departure dates for a given route.

##### Response Example

```
[
    "2021-12-22",
    "2021-07-28"
]
```

<br/>

### Get All Routes
`/api/getRoutes`

`Type: GET`

#### Description

This endpoint will return a json response with all the available routes (i.e. all the pairs of origin and destination cities).  

#### Parameters

    None

##### Request Example

`/api/getRoutes`

#### Response

A successful response will return a json response with all routes.

##### Response Example

```
[
    "seattle,san diego",
    "san francisco,boston",
    "new york,boston",
    "boston,san francisco",
    "washington d.c.,new york",
    "washington d.c.,san diego",
    "san diego,seattle",
    "san francisco,seattle",
    "san diego,washington d.c.",
    "san francisco,washington d.c.",
    "san diego,san francisco",
    "boston,washington d.c.",
    "san diego,boston",
    "new york,san diego",
    "seattle,new york"
]
```

<br/>

### Get Flights using a Route
`/api/getFlights`

`Type: GET`

#### Description

This endpoint will return all the flights for the given origin city and destination city.

#### Parameters

    - originCity (required) string
    - destinationCity (required) string

##### Request Example

`/api/getFlights?originCity=boston&destinationCity=san%20francisco`

#### Response

A successful response will return a json response with all the flights for those given cities.

##### Response Example

```
[
    {
        "flightId": 4,
        "airlineName": "america",
        "departureDate": "2021-12-22",
        "departureTime": "19:02:11",
        "numberOfStops": 2,
        "originCity": "boston",
        "destinationCity": "san francisco",
        "price": 550
    },
    {
        "flightId": 13,
        "airlineName": "southwest",
        "departureDate": "2021-07-28",
        "departureTime": "12:53:49",
        "numberOfStops": 1,
        "originCity": "boston",
        "destinationCity": "san francisco",
        "price": 681
    }
]
```
<br/>

### Get Available Seats
`/api/getSeats`

`Type: GET`

#### Description

This endpoint will return all the available seats for a given flight.

#### Parameters

    - flightID (required) int
    - isFirstClass(required) boolean

##### Request Example

`/api/getSeats?flightId=19&isFirstClass=true`

#### Response

A successful response will return all the available seats for a flight.

##### Response Example

```
[
    {
        "seatId": 2485,
        "flightId": 19,
        "seatRow": 1,
        "seatLetter": "A",
        "isAvailable": true,
        "isFirstClass": true
    },
    {
        "seatId": 2486,
        "flightId": 19,
        "seatRow": 1,
        "seatLetter": "B",
        "isAvailable": true,
        "isFirstClass": true
    },
    {
        "seatId": 2487,
        "flightId": 19,
        "seatRow": 1,
        "seatLetter": "C",
        "isAvailable": true,
        "isFirstClass": true
    }
]
```
<br/>

### Make a Reservation
`/api/makeReservation`

`TYPE: POST`

#### Description

This endpoint will allow a thirdparty end user to book a flight reservation.

#### Parameters

    - flightId (required) int
    - userId (required) int
    - seatId (required)   int
    - passengerFirstName (required) string
    - passengerLastName (required)  string

#### Request Example

`/api/makeReservation?flightId=10&userId=9&seatId=900&passengerFirstName=Fake&passengerLastName=Data`


##### Note: This updates the database, so each time you call this endpoint, the seat ID you just used will become unavailable. If you want to test this endpoint multiple times, be sure to change the seat ID.

<br/>

#### Response

- A successful response will return a 200 code and a json response with the newly created reservation information.
- If the seat is not available to book, a 200 code along with an error message will be displayed.
- If any of the ID numbers are invalid, a 404 not found code will be displayed. 



##### Response Example

```
{
    "reservationId": 27,
    "user": {
        "userId": 9,
        "userName": "clumox8",
        "password": "rZbtCC"
    },
    "firstName": "Hello",
    "lastName": "World",
    "flight": {
        "flightId": 20,
        "airlineName": "america",
        "departureDate": "2022-02-01",
        "departureTime": "19:13:23",
        "numberOfStops": 2,
        "originCity": "seattle",
        "destinationCity": "new york",
        "price": 545
    },
    "seat": {
        "seatId": 840,
        "flightId": 7,
        "seatRow": 3,
        "seatLetter": "D",
        "available": 1,
        "isFirstClass": 1
    },
    "dateCreated": "2021-06-08T03:39:37.318+00:00",
    "price": 545
}
```


### Cancel a Reservation
`/api/cancelReservation`

`TYPE: POST`

#### Description

This endpoint will allow a thirdparty end user to cancel a flight reservation using their user ID and reservation ID.

#### Parameters

    - reservationId (required) int
    - userId (required) int

#### Request Example

`/api/cancelReservation?&reservationId=19&userId=2`


##### Note 1: This updates the database, so each time you call this endpoint, the specified reservation will be deleted.
##### Note 2: The user ID entered must match the user ID associated with the reservation.
 
<br/>

#### Response

- A successful response will return the reservation ID of the cancelled reservation. 
- If the user ID and reservation ID do not match up with an existing reservation, an invalid reservation error will be displayed


##### Response Example (Success)

```
{
    "status": "Success",
    "data": 19
}
```

##### Response Example (Failure)
```
{
    "status": "Error: Invalid Reservation",
    "data": null
}
```
