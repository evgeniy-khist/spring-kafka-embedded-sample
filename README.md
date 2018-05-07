This sample demonstrates how to use the same embedded Kafka broker(s) for multiple test classes.

Run this sample using

```
./gradlew clean test -i -Dkafka.embedded.port=9092
```