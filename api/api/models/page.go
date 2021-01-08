package models

import (
	"database/sql"
	"encoding/json"
	"errors"
	"strconv"
)

type WebPage struct {
	Title string
	Link string
}

func newWebPage(title string, link string) *WebPage {
	return &WebPage{Title: title, Link: link}
}

func WebPageFromJSON(jsonString string) (*WebPage, error) {
	m := make(map[string]string)
	err := json.Unmarshal([]byte(jsonString), &m)
	if err != nil {
		return nil, err
	} else {
		title, hasTitle := m["title"]
		link, hasLink := m["link"]
		if hasTitle && hasLink {
			return newWebPage(title, link), nil
		} else {
			return nil, errors.New("JSON string is missing information")
		}
	}
}

func (this *WebPage) ToJSON() (string, error) {
	m := make(map[string]string)
	m["title"] = this.Title
	m["link"] = this.Link
	bytes, err := json.Marshal(&m)
	return string(bytes), err
}

func Query(q string) ([]WebPage, error) {
	db, err := sql.Open("pq", "host=" + dbhost + " port=" + strconv.Itoa(dbport) + " user=" + dbuser + " password=" + password + " dbname=" + dbname + " sslmode=disable")
	if err != nil {
		return nil, err
	} else {
		defer db.Close()
		rows, err := db.Query("SELECT Title, Link FROM Index WHERE Title LIKE '%" + q + "%';")
		if err != nil {
			return nil, err
		} else {
			var pages []WebPage
			for rows.Next() {
				var title, link string
				rows.Scan(&title, &link)
				pages = append(pages, WebPage{Title: title, Link: link})
			}
			return pages, nil
		}
	}
}

func (this *WebPage) AddToIndex() error {
	db, err := sql.Open("pq", "host=" + dbhost + " port=" + strconv.Itoa(dbport) + " user=" + dbuser + " password=" + password + " dbname=" + dbname + " sslmode=disable")
	if err != nil {
		return err
	} else {
		defer db.Close()
		_, err := db.Exec("INSERT INTO Index (title, link) VALUES ('" + this.Title + "', '" + this.Link + "')")
		if err != nil {
			return err
		} else {
			return nil
		}
	}
}
