# Search engine

A basic search engine

## Design

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

`GET /api/search?q=Search%20Engine`

response:

```json
[
  {
        "title": "How Search Engines Work: Crawling, Indexing, and Ranking | Beginner&#039;s Guide to SEO - Moz",
        "url": "https://moz.com/beginners-guide-to-seo/how-search-engines-operate"
  },
  {
        "title": "Web search engine - Wikipedia",
        "url": "https://en.wikipedia.org/wiki/Web_search_engine"
  }
]
```

#### Web

- Run Apache/Nginx to serve a static page that has a search bar and a section for the results
- Perform API call using AJAX to get results based on the user's search query
- Update the results section with titles and links to relevant pages

#### Infrastructure

Create Docker image for the project that will include dependencies such as:
- python3 (+ packages)
- Nginx/Apache
- PHP/Java (+ libraries)
- PostgreSQL
- etc.
