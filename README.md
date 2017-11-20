# SmartConfig

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.sergeybudnik/smart.config/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.sergeybudnik/smart.config/badge.png)

![Maven Central](https://api.travis-ci.org/SergeyBudnik/SmartConfig.svg?branch=master)

### About

TBD

### Main features ###

1. True typesafe configuration
2. Strong properties validation
3. Possibility to subscribe to properties change

### Integration

Lets imagine that you are developing a java-based server, which reads currencies from the database and distributes them to your website through Rest service.

You should be able to configure server to work in three environments: SIT, UAT, Prod
Also your production servers are located in different countries: Russia, USA and India

You want to configure:
1. Database URL
2. Language that you want to use for your Rest services response

So, lets get started!

#### First, create a space configuration file (located: ${project.basedir}/src/main/resources/config-spaces.conf)

space {
    env: [sit, uat, prod]
    location: [no_location, rus, usa, india]
}

points [
    {env: sit, location: no_location},
    {env: uat, location: no_location},
    {env: prod, location: rus},
    {env: prod, location: usa},
    {env: prod, location: india},
]

#### Now, create a configuration file with the values (located: ${project.basedir}/src/main/resources/config.conf)

database.url {
    ~sit: 'my_sit_database'
    ~uat: 'my_uat_database'
    ~prod: 'my_prod_database'
}

language {
    ~default: 'eng' // ~default - special keyword for default values
    ~rus: 'russia'
}

#### Add the following lines to your project pom.xml

```
<properties>
    <smart.config.csvProperties>${project.basedir}/src/main/resources/config.conf</smart.config.csvProperties>
    <smart.config.dimensions>${project.basedir}/src/main/resources/config-spaces.conf</smart.config.dimensions>
</properties>

<dependencies>
    <dependency>
        <groupId>com.github.sergeybudnik</groupId>
        <artifactId>smart.config.data</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>com.github.sergeybudnik</groupId>
            <artifactId>smart.config</artifactId>
            <version>2.0.0</version>
            <executions>
                <execution>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>jenesis4java</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### Finish!

Run 'mvn clean install'

After plugin execution, you will be able to work with properties in a following way:

```
SmartConfig smartConfig = SmartConfigProperties.getConfig("prod", "usa");

SmartConfigValue<String> databaseUrl = smartConfig.getDatabaseUrl();

System.out.println(databaseUrl.forceGetValue()); // Prints 'my_prod_database'
```
