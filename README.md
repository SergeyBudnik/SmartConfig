# SmartConfig

https://maven-badges.herokuapp.com/maven-central/com.github.sergeybudnik/smart.config/badge.(svg|png)?style={style}

### About

TBD

### Main features ###

1. True typesafe configuration
2. Strong properties validation
3. Possibility to subscribe to properties change

### Usage

SmartConfig requires two files to function:

1. Dimensions:

```
tier: [sit, uat]
location: [us, uk, sg]
```

2. Properties:

```
test1 {
    ~sit = 31
    ~uat = 33
}

test2 {
    ~sit = "test"
    ~uat = "user"
}

test3 {
    ~sit = [1, 2, 3]
}

test4 {
    ~sit = [a, b, c]
}
...
```

After plugin execution, you will be able to work with properties in a following way:

```
SmartConfig smartConfig = SmartConfigProperties.getConfig("sit", "uk");

SmartConfigValue<Long> test1 = smartConfig.getTest1();
SmartConfigValue<String> test2 = smartConfig.getTest2();
SmartConfigValue<List<Long>> test3 = smartConfig.getTest3();

System.out.println(test1.getValue()); // Prints '31'
```

SmartConfig allows to override properties and even subscribe to their changes

```
SmartConfig smartConfig = SmartConfigProperties.getConfig("sit", "uk");

SmartConfigValue<Long> test1 = smartConfig.getTest1();

System.out.println(test1.getValue()); // Prints '31'

test1.subscribe(it -> System.out::println); // Subscribe to value change
test1.override(32); // Override the value. Subscriber will print '32'

```

### Integration

TBD
