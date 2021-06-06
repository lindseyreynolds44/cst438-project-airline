# Flights R Us API Documentation

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
    - isFirstClass(required) int (0 or 1)

##### Request Example

`/api/getSeats?flightID=19`

#### Response

A successful response will return all the available seats for a flight.

##### Response Example

```
[
    {
        "seatId": 577,
        "flightId": 5,
        "seatRow": 7,
        "seatLetter": "A",
        "available": 1,
        "isFirstClass": 0
    },
    {
        "seatId": 578,
        "flightId": 5,
        "seatRow": 7,
        "seatLetter": "B",
        "available": 1,
        "isFirstClass": 0
    },
    {
        "seatId": 579,
        "flightId": 5,
        "seatRow": 7,
        "seatLetter": "C",
        "available": 1,
        "isFirstClass": 0
    },
]
```
<br/>

### Make a Reservation
`/api/makeReservation`

`TYPE: POST`

#### Description

This endpoint will allow a thirdparty end user to book a flight reservation.

#### Parameters

    - username (required) string
    - password (required) string
    - flightID (required) int
    - seatID (required)   int
    - passengerFirstName (required) string
    - passengerLastName (required)  string

##### Request Example

`needed`

#### Response

A successful response will return a 200 code, the reservation id, and the when booked timestamp.

##### Response Example

```

```
