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

Using these files, it generates:

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
    public class TierUat_ZoneInt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "uat-int-user";
        }
        ...
    }
    
    public interface TierUat_ZoneExt_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "uat-user";
        }
        ...
    }
    
    public interface TierUat_ZoneAny_SmartConfig implements SmartConfig {
        String getDbUser() {
            return "uat-user";
        }
        ...
    }
}
```

### Integration

TBD
