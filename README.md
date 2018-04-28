# GithubPublic

## Implementation
* Used MVP design pattern to make this app scalable considering future enhancements to this idea. I implemented following packages.
	* ##### github : 
		* Handles operations to load views after fetching data
		* Invokes calls in GithubRepository to fetch data from web
	* ##### data :
		* remote: Repository implementation to fetch data from Github API.
		* local : Database implementation to save fetched data . Not required for this task. 
		* GithubRepository : Currently has small cahcing mechanism implemented to reduce redundant requests. Strict implementation to fetch from remote server. No local cache implemented.
	* ##### network :
		* Handles Retrofit and dependency logic
	* ##### di :
		* Implements dependency injection dagger2 logic required for application.


* Used Dagger2 dependency injection to make this app testable.
* Used Mockito, JUnit, RoboElectric and Hamcrest frameworks for unit testing.
* Used Espresso for instrumentation testing.


## Libraries
* Retrofit2, RxJava2     - For fetching data from Github API
* [Dagger2](https://google.github.io/dagger/)   - For dependency injection
* [Espresso](https://github.com/googlesamples/android-testing/tree/master/ui/espresso)  - For instrumentaion testing
* [JUnit](https://mvnrepository.com/artifact/junit/junit), [Mockito](http://site.mockito.org/), [Hamcrest](http://hamcrest.org/JavaHamcrest/), [Guava](https://github.com/google/guava), RoboElectric - For Unit testing
* ButterKnife - To bind views without redundant code

## TODO
* Detailed unit tests need to be added.
* Cleaner animations.

## Author
Sarath
