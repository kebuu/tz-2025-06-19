# Project Guidelines

## Database
* All database table names must be singular
* In SQL scripts, use IF EXISTS when possible
* Don't use double quotes for database elements names
* Transaction should be opened at the Controller function level

## Code
* In respect to DDD, the domain package should only have dependency to :
    * kotlin or java packages
    * the shared package
    * the same domain package

* Do not create documentation for classes nor functions expect if asked
* When working on a specific task, do not refactor code not related to the task

## Maven
* When adding or updating version of dependencies or plugins in pom.xml, 
always try to store version numbers as properties

## Refactoring
* When moving or renaming files, use IDE refactoring tools

## Testing
* Use AssertJ for assertions
