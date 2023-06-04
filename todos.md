Todos:
- Create abstract RestModel class with requestId for keeping track of idempotent transactions
  - May want to put class in a shared core library
- Remove serializable implementation and serialVersionUID from entity classes when converting from h2 to mongo or postgres
- 