# _jLOB_
## L3 Order Book and Matching Engine

jLOB has all the basic capabilities of a functional exchange. 
Custom business logic can be defined by implementing orderbook listeners. 
It currently supports HTTP and FIX protocol communication.

There is also a [UI repository](https://github.com/eliquinox/lob-ui) that allows interacting with the orderbook.
Full sample deployment: https://lob.eliquinox.com/

### Configuration

`PersistenceLimitOrderBookListener.java` defines logic for storing orderbook events and caching orderbook state.
Events are stored in PostgreSQL and orderbook state is cached in Redis. Reference `config.local.yaml` defines parameters 
for local execution of the application:

```
database:
  host: 127.0.0.1
  name: jlob
  port: 5432
  username: postgres
  password: postgres

redis:
  host: 127.0.0.1
  port: 6379
```

Create and start these dependencies accordingly.

Reference FIX server and FIX client configurations are provided in `fix.server.cfg` and `fix.client.cfg` accordingly. 

### Quickstart

#### Local Development

To start the service:

`gradle runBook`

The task will create needed JOOQ classes and run DB migrations, creating needed tables.
It will then start HTTP and FIX servers on `http://127.0.0.1:4567` and `tcp://127.0.0.1:9877`.

#### Docker Deployment

The repository includes Docker support for easy deployment. Before deploying with Docker, you need to build the application JAR locally:

```bash
gradle bookJar -x test -x generateJLobJooqSchemaSource
```

Then, deploy the entire stack (application, PostgreSQL, and Redis) using Docker Compose:

```bash
docker-compose up -d
```

This will:
- Build the application Docker image
- Start PostgreSQL database on port 5432
- Start Redis cache on port 6379
- Start the jLOB application on ports 4567 (HTTP) and 9877 (FIX)

**⚠️ Security Note**: The default `docker-compose.yml` uses insecure default passwords for local development. For production deployments, change all passwords and consider using Docker secrets or environment-specific configuration files.

To view logs:
```bash
docker-compose logs -f jlob
```

To stop the services:
```bash
docker-compose down
```

##### Environment Variables

The application supports the following environment variables for configuration:

- `DB_HOST` - Database host (default: localhost)
- `DB_NAME` - Database name (default: jlob)
- `DB_PORT` - Database port (default: 5432)
- `DB_USER` - Database username (default: postgres)
- `DB_PASSWORD` - Database password (default: postgres)
- `REDIS_HOST` - Redis host (default: localhost)
- `REDIS_PORT` - Redis port (default: 6379)
- `LBANK_API_KEY` - Lbank API key for exchange connectivity (optional)

These can be customized in the `docker-compose.yml` file or passed at runtime.

#### Deployment Remote Setup

To set up a deployment remote repository for managing deployment artifacts:

```bash
./deploy.sh
```

This script will:
- Add a `deployment` remote pointing to the deployment repository (git@github.com:IAMAB11/Deployment.git)
- Display current remotes
- Provide instructions for building and pushing to the deployment repository

**Note**: The remote is named `deployment` to avoid conflicts with the existing `origin` remote that points to this repository.

Alternatively, you can manually add the deployment remote:

```bash
git remote add deployment git@github.com:IAMAB11/Deployment.git
git remote -v
```

### Lbank API Connectivity

The repository includes a Python script to verify connectivity to the Lbank exchange API.

#### Prerequisites

Install Python dependencies:

```bash
pip install -r quantum-hft-enterprise/examples/requirements.txt
```

#### Running the Connectivity Check

Set your Lbank API key and run the connectivity check script:

```bash
export LBANK_API_KEY=your-api-key-here
python quantum-hft-enterprise/examples/lbank_connectivity_check.py
```

The script will:
- Validate the LBANK_API_KEY environment variable is set
- Check connectivity to Lbank public API endpoints
- Retrieve available trading pairs
- Fetch sample ticker information

**Note**: The connectivity check uses public Lbank API endpoints that don't require authentication. For authenticated endpoints, additional configuration may be needed.

### API Endpoints

Import `jLOB.postman_collection.json` into [Postman](https://www.getpostman.com/) to start exploring the API.

_jLOB_ supports the following endpoints:

`GET /book`

Retrieves the current state of the orderbook:

*Example response*:

```
{
    "bids": {
        "90": {
            "side": "BID",
            "price": 90,
            "placements": [
                {
                    "uuid": "77b778eb-4304-41f8-9a75-3113c459907a",
                    "timestamp": {
                        "seconds": 1587682193,
                        "nanos": 398559000
                    },
                    "side": "BID",
                    "price": 90,
                    "size": 100
                }
            ]
        }
    },
    "offers": {
        "100": {
            "side": "OFFER",
            "price": 100,
            "placements": [
                {
                    "uuid": "6a6a90cc-3f7d-4514-b70e-dca1e1706415",
                    "timestamp": {
                        "seconds": 1587682203,
                        "nanos": 9953000
                    },
                    "side": "OFFER",
                    "price": 100,
                    "size": 100
                }
            ]
        }
    }
}
```

`POST /book`

Sends an order to the orderbook.

_Example Request:_

```
{
    "side": "offer",
    "price": 100,
    "size": 100000
}
```

_Example Response:_

```
{
    "uuid": "e08f8a3b-f38b-4a72-a2a9-2ae0cb8c9d1f",
    "timestamp": {
        "seconds": 1587682282,
        "nanos": 259523000
    },
    "side": "OFFER",
    "price": 100,
    "size": 10000
}
```

`DEL /book`

Cancels a given amount of an existing order.

_Example Request_:

```
{
    "id": "e08f8a3b-f38b-4a72-a2a9-2ae0cb8c9d1f",
    "size": 20
}
```

_Example Response_:

```
{
    "placementUuid": "e08f8a3b-f38b-4a72-a2a9-2ae0cb8c9d1f",
    "timestamp": {
        "seconds": 1587682397,
        "nanos": 728419000
    },
    "size": 20
}
```

`POST /vwap`

Retrieves a volume-weighted average price from the orderbook, given a side and a size of a hypothetical order.

_Example Request:_

```
{
    "action": "bid",
    "size": 10000
}
```

_Example Response:_

```
{
    "price": 100.00
}
```
