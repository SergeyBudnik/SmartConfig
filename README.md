# SmartConfig

### About

TBD

### Usage

SmartConfig requires two files to function:

1. Dimensions:

```
tier: ['sit', 'uat', 'prod']
location: ['uk', 'us', 'sg']
zone: ['int', 'ext']
```

2. Properties:

```
db.user {
    ~dev: 'dev-user'
    ~sit: 'sit-user'
    ~sit~int: 'sit-int-user'
    ~default: 'user'
}
```

Using these files, it generates:

1. Dimensions class:

```java
public class SmartConfigDimension {
    public interface TIER {
        static final String PROD = "PROD";
        static final String UAT = "UAT";
        static final String SIT = "SIT";
    }
    
    public interface LOCATION {
        static final String SG = "SG";
        static final String UK = "UK";
        static final String US = "US";
    }
    
    public interface ZONE {
        static final String INT = "INT";
        static final String EXT = "EXT";
    }
}
```

### Integration

TBD
