# SmartConfig

### About

TBD

### How it works

SmartConfig requires two files to function:

1. Dimensions

```
tier: ['sit', 'uat', 'prod']
location: ['uk', 'us', 'sg']
zone: ['int', 'ext']
```

2. Properties

```
db.user {
    ~dev: 'dev-user'
    ~sit: 'sit-user'
    ~sit~int: 'sit-int-user'
    ~default: 'user'
}
```

### Usage

TBD
