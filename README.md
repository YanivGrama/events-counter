# Events Counter App
An events counter application.

The application reads events from `stdin` and collects statistics (words/types count).

## Instructions

1) build project - `mvn clean install`
2) execute the events script and execute the server
    * for **Mac OS X** - ```./scripts/generator-macosx-amd64 | java -jar ./target/eventscounter-1.0-SNAPSHOT-jar-with-dependencies.jar```
    * for **Linux** - ```./scripts/generator-linux-amd64 | java -jar ./target/eventscounter-1.0-SNAPSHOT-jar-with-dependencies.jar```
    * for **Windows** - ```./scripts/generator-windows-amd64.exe | java -jar ./target/eventscounter-1.0-SNAPSHOT-jar-with-dependencies.jar```
3) go to http://localhost:8080
4) Enjoy life!

## Short Description

### Actors Pipeline
[Raw Events] -> [Parsed Events] -> [Countable Events] -> [Statistic Updater] 

## Things for improvement (TODO LIST)
* Use dependency injection framework
* Use external queue like Kafka for better back-pressure handling
* Use external DB for better scalability (support fot multiple app instances) and keep app stateless

## UI
http://localhost:8080/ 
![main ui](https://github.com/YanivGrama/events-counter/blob/master/images/img_1.png)

http://localhost:8080/wordsCount 
![words count GET](https://github.com/YanivGrama/events-counter/blob/master/images/img_2.png)

http://localhost:8080/eventTypesCount
![event types count GET](https://github.com/YanivGrama/events-counter/blob/master/images/img_3.png)
