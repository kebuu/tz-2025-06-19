# Project Guidelines

## Database
* All database table names must be singular
* In SQL scripts, use IF EXISTS when possible
* Don't use double quotes for database elements names

## Code
* In respect to DDD, the domain package should only have dependency to :
    * kotlin or java packages
    * the shared package
    * the same domain package

* Do not create documentation for classes nor functions expect if asked
* When working on a specific task, do not refactor code not related to the task

## Refactoring
* When moving or renaming files, use IDE refactoring tools

## Testing
* Use AssertJ for assertions
