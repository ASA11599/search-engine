#!/usr/bin/env python3

import requests as rqs
import os
import sys
import html.parser as hp
from queue import Queue
import re
import time

url_regex = re.compile(
                r'^(?:http|ftp)s?://'
                r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|'
                r'localhost|'
                r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})'
                r'(?::\d+)?'
                r'(?:/?|[/?]\S+)$', re.IGNORECASE
            )

def is_url(url: str) -> bool:
    return (re.match(url_regex, url) is not None)

class MyHTMLParser(hp.HTMLParser):

    q: Queue = Queue(0)

    def handle_starttag(self, tag, attrs):
        for attr_t in attrs:
            if attr_t[0] == 'href':
                if is_url(attr_t[1]):
                    MyHTMLParser.q.put(attr_t[1])
                    print("Found link to: " + str(attr_t[1]))

def main():
    return
    if not (("CRAWL_FOR" in os.environ.keys()) and ("START_AT" in os.environ.keys())):
        print("Error: CRAWL_FOR or START_AT environment variables are missing")
    else:
        print("web crawler started!")
        crawl_time: int = int(os.environ["CRAWL_FOR"])
        start_page: str = os.environ["START_AT"]
        print("starting at " + start_page + " and crawling for " + str(crawl_time) + " seconds")
        print()
        MyHTMLParser.q.put(start_page)
        start_time: int = int(time.time())
        end_time: int = start_time + crawl_time
        done: bool = int(time.time()) >= end_time
        while (not MyHTMLParser.q.empty()) and (not done):
            try:
                res: rqs.Response = rqs.get(MyHTMLParser.q.get())
                parser: hp.HTMLParser = MyHTMLParser()
                parser.feed(res.text)
            except Exception as e:
                print("Something went wrong...")
                print(e)
            done = int(time.time()) >= end_time

if __name__ == "__main__":
    main()
