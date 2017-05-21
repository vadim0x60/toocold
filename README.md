# Too cold; didn't move

An incredibly simple API to look up how good a city's climate is.

## Why

Once on a cold and rainy Moscow evening, I asked myself which city on Earth has the most comfortable (for me) temperature range. I pulled up the browser: surely there must be a search engine for that, right? Some API that lets you search by highest/lowest yearly temperature? Turns out, the best source available for this kind of data is Wikipedia weatherboxes: they were hand gathered from websites of various government agencies and coerced to a standard format. 

"Too cold; didn't move" is a tool that parses Wikipedia weatherboxes and exposes a very brief summary of each city's climate as an API to facilitate all kinds of research. It could be integrated into some search engine for cities like [NomadList](https://nomadlist.com/), for example.

## How to use it

Request

```
curl -X GET --header 'Accept: application/json' 'https://toocold.herokuapp.com/climate?city=Budapest'
```

Response

```
{
  "city": "Budapest",
  "upper-bound": 26.7,
  "average-high": 15.325000000000001,
  "average-low": 8.649999999999999,
  "lower-bound": 0
}
```

`application/edn`, `application/x-yaml` work as well. Temperatures are in Celcius. For US cities, temperatures are converted to Celcius. We only do Celcius here. If there's any ambiguity on what to call a city, just check with Wikipedia: city names are just names of their corresponding articles.

There is also [Swagger-based interactive documentation](https://toocold.herokuapp.com/).

## Known issues

1. Perth, Australia is not currently supported. In the meantime, feel free to use [the original weatherbox](https://en.wikipedia.org/wiki/Perth#Climate).
2. There is no caching strategy yet: all requests go straight to Wikipedia. If you're making a lot of requests, please cache on your side. This helps the environment, Wikipedia and my free Heroku dyno.

## License

Copyright © 2017 Vadim Liventsev

Distributed under the MIT License, see `LICENSE`.
