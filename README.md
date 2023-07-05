# Hawtio React

An example camel springboot app

## Overview
Implements a camel route that fetches all the commits from the hawtio/hawtio repository (3.x branch) and exports them as json files to the local filesystem (${HOME}/camel-github).

## Install
Please go to the settings of your github user [profile](https://github.com/settings/tokens?type=beta) and
create a new developer personal access token, eg.

```
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
```

Then run the following:
```
./start.sh -t <github auth token>
```

## Hawtio
The application contains the [hawtio](https://github.com/hawtio/hawtio) console and can be accessed using
the url -> [http://localhost:10001/actuator/hawtio](http://localhost:10001/actuator/hawtio).

A connection is **not** required to be added. Simply click on the *JMX* or *Camel* to display the application details.



