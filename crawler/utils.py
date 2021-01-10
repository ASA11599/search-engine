import re
import requests
import json
import bs4
import urllib.parse

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

def add_page_to_index(title: str, link: str) -> bool:
    try:
        res: requests.Response = requests.post(
                                "http://api-server/index",
                                headers={"Content-type": "application/json", "Authorization": "Basic YWRtaW46YWRtaW4="},
                                data=json.dumps({"title": title, "link": link})
                            )
        # Verify that the resource was created
        return res.status_code == 201
    except:
        return False

def find_page_info(url: str) -> tuple:
    try:
        html: str = requests.get(url).text
        soup: bs4.BeautifulSoup = bs4.BeautifulSoup(html, "html.parser")
        links: list = []
        for e in soup.findAll("a"):
            href: str = e.get("href")
            parsed_url: urllib.parse.ParseResult = urllib.parse.urlparse(url)
            current_host: str = parsed_url.netloc
            current_protocol: str = parsed_url.scheme
            if not href is None:
                if href.startswith("//"):
                    links.append(current_protocol + ":" + href)
                elif href.startswith("/"):
                    links.append(current_protocol + ":" + current_host + href)
                else:
                    links.append(href)
        if not (soup.find("title") is None):
            return (soup.find("title").string, links)
        else:
            return (None, links)
    except requests.exceptions.ConnectionError:
       return None
