# Search engine

A basic search engine

## Design

![design](https://raw.githubusercontent.com/ASA11599/search-engine/master/search-engine_design.png "Design")

#### Web crawler

- Web crawler will parse a web page and add all of its links to a queue
- Do the same for each element on the queue
- Store each URL and its title if not already found

#### Database

Use a simple schema like the following:

| ID       | Title  | URL                    |
|----------|--------|------------------------|
| 74392724 | Google | https://www.google.com |
| 74392725 | Amazon | https://www.amazon.ca  |
| ...      | ...    | ...                    |

#### API

Create an API to return a list of pages from a query like such:

`GET /api/index?q=Search%20Engine`

response:

```json
[
  {
        "Title": "How Search Engines Work: Crawling, Indexing, and Ranking | Beginner&#039;s Guide to SEO - Moz",
        "Link": "https://moz.com/beginners-guide-to-seo/how-search-engines-operate"
  },
  {
        "Title": "Web search engine - Wikipedia",
        "Link": "https://en.wikipedia.org/wiki/Web_search_engine"
  }
]
```

#### Web

- Run Nginx to serve a static page that has a search bar and a section for the results
- Perform API call using AJAX to get results based on the user's search query
- Update the results section with titles and links to relevant pages

#### Infrastructure

The project is made up of the following services:

- A web server for static pages, assets and redirects to the API
- An API server that will handle all reads and writes to the database
- A web crawler to look for pages on the web
- A database to store pages found by the crawler

Each of these services will run in a Docker container with all is dependencies.
