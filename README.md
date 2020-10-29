# API-server

Simple API server for my allergenie web app.

## Installation

Download from https://github.com/Romacoding/allergenie-api

## Usage

Make sure you have Clojure installed on your system.

Run the project directly:

    $ clj -M -m allergenie.api-server

Run the project's tests (they'll fail until you edit them):

    $ clj -M:test:runner

Build an uberjar:

    $ clj -M:uberjar

Run that uberjar:

    $ java -jar api-server.jar


## Routes

/pollen - get pollen info for the provided zip code.

/air - get air info for the provided zip code.

/weather - get weather info for the provided zip code.

Example: Request to /pollen?zip=11223 will receive the pollen info for zip 11223 in a JSON format.

### Bugs
### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 Roman Ostash

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
