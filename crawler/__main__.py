#!/usr/bin/env python3

import os

if __name__ == "__main__":
    print("web crawler started!")
    print("crawling for " + str(os.environ["CRAWL_FOR"]) + " seconds ...")
