# Java SDK 101

his introductory training will guide you through the basics of the GreyCat Python SDK.

## Prerequisites

### Python

- Java >= 8
- Maven 3

### GreyCat setup

- GreyCat runtime: https://get.greycat.io/
- Java:
  - Maven:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project>
      […]
      <properties>
        […]
        <greycat.version.branch>dev</greycat.version.branch>
        <greycat.version.major>6.3</greycat.version.major>
        <greycat.version.minor>9</greycat.version.minor>
        <greycat.version>${greycat.version.major}.${greycat.version.minor}-${greycat.version.branch}</greycat.version>
      </properties>
      […]
      <repositories>
        […]
        <repository>
          <name>GreyCat Java SDK repository</name>
          <id>get.greycat.io</id>
          <url>https://get.greycat.io/files/sdk/java/${greycat.version.branch}/${greycat.version.major}/</url>
          <layout>default</layout>
        </repository>
      </repositories>
      […]
    </project>
    ```
  - Gradle:
    ```json
    TODO
    ```
As the version above is doomed to be outdated, more recent versions can be checked at https://get.greycat.io/files/sdk/java/testing/

## GreyCat server application

The server consists of an example dataset (a `nodeList` of 10 integers) and three endpoints.

- The GreyCat server is started with:
  ```bash
  greycat serve --user=1
  ```
- In Java, the following code instantiates a client to the GreyCat server:
  ```java
  import ai.greycat.*;

  public final class HelloWorld {
    public static void main(String... args) throws Exception {
      GreyCat greycat = new GreyCat("http://localhost:8080");
    } 
  }
  ```

### Hello, World!

- Providing an endpoint is as simple as annotating any function with `@expose`:
  ```gcl
  @expose
  fn helloWorld() {
    println("Hello, World!");
  }
  ```
- Then you can call the endpoint in Java with the following code, considering the helloWorld function is stored in `project.gcl`:
  ```java
  […]
  GreyCat.call(greycat, "project::helloWorld");
  […]
  ```
- Expectedly, this call results in a greeting printed on GreyCat server logging stack.
- To run this test from command-line:
  ```bash
    mvn package exec:java -Dexec.mainClass=HelloWorld
  ```

### Getting data

- Endpoints may yield results, for instance the following returns the dataset as an array:
  ```gcl
  @expose
  fn getData(): Array<int> {
    var res = Array<int>::new(data!!.size());
    for (index, value in data?) {
      res[index] = value;
    }
    return res;
  }
  ```
- Expectedly, in Java data can be easily gathered with:
  ```java
  […]
  std.core.Array<?> data = (std.core.Array<?>) GreyCat.call(greycat, "project::getData");
  […]
  ```
- GreyCat integers, as they are stored signed on 64 bits, map to `long` integers in Java:
  ```java
  […]
  System.out.println("// Data:" + data);
  // Data:[0,1,2,3,4,5,6,7,8,9]
  System.out.println("// Type: " + data.getClass() + "<" + data.iterator().next().getClass() + ">");
  // Type: class ai.greycat.std$core$Array<class java.lang.Long>
  […]
  ```
- To run this test from command-line:
  ```bash
    mvn package exec:java -Dexec.mainClass=GetData
  ```

### Sending data

- Conversely, GreyCat endpoints may also accept any number of parameters:
  ```gcl
  @expose
  fn greet(firstName: String, lastName: String): String {
    var greeting = "Hello, ${firstName} ${lastName}!";
    println(greeting);
    return greeting;
  }
  ```
- In Java, the call method of the GreyCat class accepts trailing variadic parameters to be sent to the GreyCat endpoint:
  ```java
  […]
  String greeting = (String) GreyCat.call(greycat, "project::greet", "John", "Doe");
  System.out.println(greeting);
  […]
  ```
- This code will greet John Doe both on GreyCat server and Java client sides.
- To run this test from command-line:
  ```bash
    mvn package exec:java -Dexec.mainClass=Greet
  ```