# Download

PrimeFaces has a single JAR called **primefaces-{version}.jar**. There are two ways to download this
JAR, you can either download from PrimeFaces homepage or if you are a Maven user you can define
it as a dependency.

## Download with Maven
Group id is _org.primefaces_ and artifact id is _primefaces._

### javax (JSF 2.0 - JSF 2.3)

```xml
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>10.0.0</version>
</dependency>
```

### jakarta (JSF 3.0)

```xml
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>10.0.0</version>
    <classifier>jakarta</classifier>
</dependency>
```