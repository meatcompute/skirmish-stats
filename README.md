# skirmish-stats

Analytics tools for competitive EVE Online players.

## Usage

Database, run:
`bin/run -m datomic.peer-server -h localhost -p 8998 -a myaccesskey,mysecret -d skirmish-stats,datomic:dev://localhost:4334/skirmish-stats`
`bin/transactor config/dev-transactor-template.properties`

Still a fresh project, not intended for use.

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
