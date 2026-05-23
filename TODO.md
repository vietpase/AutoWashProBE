# TODO

## High Priority

- [ ] Implement REST controllers for all entities
- [ ] Configure Spring Security filter chain (JWT authentication)
- [ ] Complete login logic in `AuthService`
- [ ] Add CRUD endpoints for Customer management
- [ ] Add CRUD endpoints for Booking management
- [ ] Add CRUD endpoints for Vehicle management

## Medium Priority

- [ ] Implement loyalty tier auto-upgrade logic based on monthly stats
- [ ] Add Promotion validation (date overlap, tier eligibility)
- [ ] Implement point expiry scheduling
- [ ] Add reward redemption validation
- [ ] Create admin dashboard endpoints (stats, reports)
- [ ] Add pagination, sorting, and filtering to list endpoints

## Low Priority

- [ ] Write comprehensive unit/integration tests
- [ ] Add API rate limiting
- [ ] Set up CI/CD pipeline
- [ ] Add logging framework (SLF4J / Logback configuration)
- [ ] Dockerize the application
- [ ] Add environment-specific configuration (dev/staging/prod)
- [ ] Create Postman collection for API testing

## Known Issues

- Repository package name is misspelled as `respository` (should be `repository`)
- Missing `AdminAccountRepository`, `VehicleRepository` — currently only 3 repos exist
- Login endpoint not yet exposed
