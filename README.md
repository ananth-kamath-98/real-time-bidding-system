# real-time-bidding-system
This repository contains a minimal Real-Time Bidding (RTB) prototype designed to highlight key infrastructure patterns. It simulates an end-to-end ad auction pipeline—publishing impressions, collecting bids, running mini-auctions, and serving the winning ad—all with a small footprint. By focusing on messaging, scheduling, and fast state storage, this project demonstrates how a production-scale RTB system can evolve from a handful of simple building blocks.

For a deep dive into the design rationale, architecture, and step-by-step walkthrough, see the full article: [Deconstructing Real‐Time Bidding: A Lightweight Approach](https://medium.com/@ananthkamath98/deconstructing-real-time-bidding-a-lightweight-approach-3d4f727f1d7d)


## Prerequisites

- **Docker & Docker Compose**  
  (to run RabbitMQ and Redis containers)

- **JDK 11+**  
  (for running Spring Boot services)

- **Git**  
  (to clone this repository)


## Quick Start

1. **Launch RabbitMQ and Redis**

    ``` 
    docker-compose up -d
    ```

    - RabbitMQ Management UI: `localhost:15672` (User: `guest`, pass: `guest`)
    - Redis: listening on `localhost:6379`

2. **Run Each Microservice (in separate terminal windows):**
While being in the root directory of the repository
- Impression-Publisher
    ```
    cd impression-publisher
    ./mvnw spring-boot:run
    ```
- Bidder-Service #1 (Listening on bidder-1-queue)
    ```
    cd bidding-service
    ./mvnw spring-boot:run -Drtb.queue.impressions=bidder-1-queue
    ```

- Bidder-Service #2 (Listening on bidder-2-queue)
    ```
    cd bidding-service
    ./mvnw spring-boot:run -Drtb.queue.impressions=bidder-2-queue
    ```

- Auction Engine (default port: 8081)
    ```
    cd auction
    ./mvnw spring-boot:run
    ```

- Ad Server (default port 8090)
    ```
    cd adserver
    ./mvnw spring-boot:run
    ```

## Verifying Setup

1. **Publish an Impression**
    ```
    curl -X POST http://localhost:8080/impressions \
    -H "Content-Type: application/json" \
    -d '{"impressionId":"imp-123","floor":50}'
    ```
    You should receive a `202 Accepted` response.

2. **Check RabbitMQ**
- Visit `http:localhost:15672` and login.
- Under Queues, you'll see `bidder-1-queue`, `bidder-2-queue`, and `bids` queues.
- Under Exchanges, you'll see `impression-exchange` and `bids-exchange`.

3. **Observe Auction and Winner Storage**
- In the Auction Engine console, you’ll see logs showing bids buffered and the winner selected.
- In a new terminal, run:
    ```
    redis-cli GET "winners:imp-123"
    ```
    You should get the winning bid.

4.**Retrieve the Winning Ad**
In a new terminal, run:
```
curl http://localhost:8090/winning-ad?impressionId=imp-123
```
Returns the stored winner or 404 if no result yet.

## Configuration Files
- docker-compose.yml
Launches RabbitMQ and Redis in a docker container.

- definitions.json
Pre-declares exchanges (impression-exchange, bids-exchange), queues (bidder-1-queue, bidder-2-queue, bids), and necessary bindings.

All other configuration settings (ports, queue names, delays, etc..) live in each services' `src/main/resources/application.yml`

## License
This project is licensed under the MIT License. See the [LICENSE](https://opensource.org/license/MIT) file for details.
