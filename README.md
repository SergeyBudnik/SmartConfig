# SmartConfig

### About

TBD

### Usage

SmartConfig requires two files to function:

1. Dimensions:

```
tier: ['uat', 'prod']
zone: ['int', 'ext']
```

2. Properties:

```
db.user {
    ~uat: 'uat-user'
    ~uat~int: 'uat-int-user'
    ~default: 'user'
}
...
```

Using these files, SmartConfig plugin generates:

1. Dimensions class:

```java
public class SmartConfigDimension {
    public interface Tier {
        static final String PROD = "PROD";
        static final String UAT = "UAT";
    }
    
    public interface Zone {
        static final String INT = "INT";
        static final String EXT = "EXT";
    }
}
```

2. Schema class

```java
public interface SmartConfig {
    String getDbUser();
}
```

3. Properties distribution class

```java
public class SmartConfigDistribution {
    class TierUat_ZoneInt_SmartConfig implements SmartConfig {
        private static final SmartConfigProperty<String> v = new SmartConfigProperty<String>("uat-int-user");
    
        public SmartConfigProperty<String> getDbUser() {
            return v;
        }
        ...
    }
    
    ... and so on ...
    
    public SmartConfig get(Optional<String> tier, Optional<String> zone) {
        // Smart code goes here
    }
}
```

So now you just need to get a property your configuration instance:

```java
...
SmartConfig smartConfig = new SmartConfigDistribution().get(Optional.of("PROD"), Optional.empty());
String dbUser = smartConfig.getDbUser().val();
...
```

And if you want to override a property, simply do:

```java
...
smartConfig.getDbUser().override("hello!");
...
```

### Integration

TBD
