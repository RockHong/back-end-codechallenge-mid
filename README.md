
How to Run
========
## Start the server
- In a terminal, go to root directory of this project.
- Type `./gradlew bootRun`

## Test the REST API
### Get cart
    curl localhost:8001/cart/1

### Add/update item to/in cart
Add an item to the cart.

    curl -X POST -H "Content-Type: application/json" \
        -d '{"productId": 2, "quantity": "3"}' \
        localhost:8001/cart/1/items

Update the quantity of the item in the cart (using POST).

    curl -X POST -H "Content-Type: application/json" \
        -d '{"productId": 2, "quantity": "6"}' \
        localhost:8001/cart/1/items

Update the quantity of the item in the cart (using PUT).

    curl -X PUT -H "Content-Type: application/json" \
        -d '{"quantity": "66"}' \
        localhost:8001/cart/1/items/1

### Empty cart
    curl -X DELETE -H "Content-Type: application/json" localhost:8001/cart/1/items

### Place order
    curl -X POST -H "Content-Type: application/json" \
        -d '{"paymentMethod": {"cardNumber": "123", "type":"CREDIT_CARD"}}' \
        localhost:8001/cart/1/placeOrder

### Add non-existed product
    curl -X POST -H "Content-Type: application/json" \
        -d '{"productId": 22222, "quantity": "3"}' \
        localhost:8001/cart/1/items

## Connect the H2 database
In a browser, open http://localhost:8001/h2-console .
Check the `applicaton.properties` file for the credentials.

Copy of original README.md
==========
## Hey there! 👋


Thanks for your interest in joining the Thrive Dev team. In order for us to better evaluate your experience, skills, and fit for our team, we'd like you complete a small coding challenge designed to test the abilities of mid-senior level back-end engineers


## The challenge

You are building out an ordering API for an e-commerce platform. You're working on the user story "As a user, I can submit my order and payment details to the system, in order to complete my purchase.""

## The acceptance criteria are:
### Functional requirements

- Create a cart, add items, remove items. Total price is retained at current prices of the items, not the price at the time of being added to the cart. 
    - Items added to cart must exist
    - Items added to cart must be in stock

- Place an order by providing payment details: cardNo, expiryDate, CVC no, and cartNumber. 
    - Items in cart must be in stock at time of order
    - Cart instance should be converted to an order
        - Order should now have itemized static prices equal to price at time of order
        - Total reflects the cost of items at time of order
    - Returning order number and order status, and total charge. 

### Non-functional requirements

- **API calls must be idempotent**
- You *must*:
    - use Typescript in node.js OR a statically-typed language of your choice
    - use a REST or graphQL (preferred) as the API layer
    - a persistence layer
- The rest of your implementation is down to you (libraries, frameworks, etc)

## Guidance

- Implement best practices of API design
- Consider concurrency
- Read all the requirements. Assessment will be made based on number of requirements satisfied

**For your code submission, it's preferred that you send us a link to a GIT repository showing your personal commits, rather than a zip file, but a ZIP is acceptable. Your submission should include:**

- the code

- a complete README on how to run the code, including any environment variables

- if you use an RDBM persistence layer , provide the SQL schema as a SQL file


Thank you! Happy coding :)
