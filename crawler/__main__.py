#!/usr/bin/env python3

import os
import sys
from queue import Queue
import time

import utils


def main():
    # TODO: grab multiple pages from $START_AT and crawl on multiple threads
    if not (("CRAWL_FOR" in os.environ.keys()) and ("START_AT" in os.environ.keys())):
        print("Error: CRAWL_FOR or START_AT environment variables are missing")
    else:
        print("web crawler started!")
        crawl_time: int = int(os.environ["CRAWL_FOR"])
        start_page: str = os.environ["START_AT"]
        print("starting at {} and crawling for {} seconds".format(start_page, crawl_time))
        print()
        q: Queue = Queue()
        q.put(start_page)
        start_time: int = int(time.time())
        end_time: int = start_time + crawl_time
        done: bool = int(time.time()) >= end_time
        while (not q.empty()) and (not done):
            current_page_url: str = q.get()
            if utils.is_url(current_page_url):
                page_info: tuple = utils.find_page_info(current_page_url)
                if not (page_info is None):
                    for link in page_info[1]:
                        q.put(link)
                    if (not (page_info[0] is None)) and (utils.is_url(current_page_url)):
                        if not utils.add_page_to_index(page_info[0], current_page_url):
                            print("Error: unable to add page {} to index".format(current_page_url))
                        else:
                            print("added {} to index ({})".format(page_info[0], current_page_url))
            time.sleep(3)
            done = int(time.time()) >= end_time

if __name__ == "__main__":
    main()
