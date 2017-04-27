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
        String getDbUser() {
            return "uat-int-user";
        }
        ...
    }
    
    interface TierUat_ZoneExt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "uat-user";
        }
        ...
    }
    
    interface TierUat_ZoneAny_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "uat-user";
        }
        ...
    }
    
    class TierProd_ZoneInt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    class TierProd_ZoneExt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    class TierProd_ZoneAny_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    public class TierAny_ZoneInt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    class TierAny_ZoneExt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    class TierAny_ZoneAny_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "user";
        }
        ...
    }
    
    public SmartConfig get(Optional<String> tier, Optional<String> zone) {
        // Smart code goes here
    }
}
```

So now you just need to get a property your configuration instance:

```java
...
SmartConfig smartConfig = new SmartConfigDistribution().get(Optional.of("PROD"), Optional.empty());
String dbUser = smartConfig.getDbUser();
...
```

### Integration

TBD
