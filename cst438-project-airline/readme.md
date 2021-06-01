# Flights R Us API Documentation

## Endpoints

### `api/getFlights`

`Type: GET`

#### Description

This endpoint will return all the flights for the given origin city and destination city.

#### Parameters

    - originCity (required) string
    - destinationCity (required) string

##### Request Example

`api/getFlights?originCity=boston&destinationCity=san francisco`

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

### api/getSeats

#### Description

This endpoint will return all the available seats for a given flight.

#### Parameters

    - flightID (required) int
    - isFirstClass(required) int (0 or 1)

##### Request Example

`api/getSeats?flightID=19`

#### Response

A successful response will return all the available seats for a flight.

##### Response Example

```
[
    {
        "seatId": 577,
        "flight": {
            "flightId": 5,
            "airlineName": "alaska",
            "departureDate": "2021-09-16",
            "departureTime": "16:27:59",
            "numberOfStops": 2,
            "originCity": "washington d.c.",
            "destinationCity": "new york",
            "price": 253
        },
        "seatRow": 7,
        "seatLetter": "A",
        "available": 1,
        "isFirstClass": 0
    },
    {
        "seatId": 578,
        "flight": {
            "flightId": 5,
            "airlineName": "alaska",
            "departureDate": "2021-09-16",
            "departureTime": "16:27:59",
            "numberOfStops": 2,
            "originCity": "washington d.c.",
            "destinationCity": "new york",
            "price": 253
        },
        "seatRow": 7,
        "seatLetter": "B",
        "available": 1,
        "isFirstClass": 0
    },
]
```

### `api/makeReservation`

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
