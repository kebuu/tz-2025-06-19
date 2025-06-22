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
* In domain package avoid using primitive types when a value class is available

### Value classes
* When `value class` has specific constraints or methods, create a specific test class for them to validate these


## Maven
* When adding or updating a version of dependencies or plugins in pom.xml, 
always try to store version numbers as properties

## Refactoring
* When moving or renaming files, use IDE refactoring tools

## Testing
* Use AssertJ for assertions
* When you need to validate all properties of an object, use a single statement like 
 `Assertions.assertThat(actualObject).isEqualTo(expectedObject)` instead of many statements like
 ``` 
 assertThat(actualObject.propery1).isEqualTo(expectedValue1)
 assertThat(actualObject.propery2).isEqualTo(expectedValue2)
 assertThat(actualObject.propery3).isEqualTo(expectedValue3)
 ```
* When you need to validate a list of elements, use a single statement like
 `Assertions.assertThat(actualList).isEqualTo(expectedObject)` instead of many statements like
 ``` 
 assertThat(actualList).hasSize(n)
 assertThat(actualList[0]).isEqualTo(expectedObject0)
 assertThat(actualList[1]).isEqualTo(expectedObject1)
 ...
 assertThat(actualList[n]).isEqualTo(expectedObjectn)
 ```
* When you need to validate a list of elements, you can also use alternative like: containsExactlyInAnyOrder,
  containsAnyElementsOf,extracting, flatExtracting...
* When a DTO contains validation annotations, don't create a test class directly for that DTO but add a test 
function for the endpoint in which it is used and verify that all validations are handled properly (is possible in a
single test function)

## Git
## Git
* Always stage the files you create to git
* Before submitting a solution, run `git status` to check for untracked files and stage them with `git add <file_path>`
* Include all newly created files in your commits, even test files and supporting classes
* When implementing a feature that requires multiple files, stage each new file immediately after creation
