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
	wp := new(WebPage)
	err := json.Unmarshal([]byte(jsonString), wp)
	if err != nil {
		return nil, err
	} else {
		if (wp.Title == "") || (wp.Link == "") {
			return nil, errors.New("Information missing from JSON, needs title and link")
		} else {
			return wp, nil
		}
	}
}

func (this *WebPage) ToJSON() (string, error) {
	bytes, err := json.Marshal(this)
	return string(bytes), err
}

func Query(q string) ([]WebPage, error) {
	db, err := sql.Open("pq", "host=" + dbhost + " port=" + strconv.Itoa(dbport) + " user=" + dbuser + " password=" + password + " dbname=" + dbname + " sslmode=disable")
	if err != nil {
		return nil, err
	} else {
		defer db.Close()
		// TODO: sanitize input (this is vulnerable to SQL injection !!!)
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
		// TODO: sanitize input (this is vulnerabel to SQL injection !!!)
		_, err := db.Exec("INSERT INTO Index (title, link) VALUES ('" + this.Title + "', '" + this.Link + "')")
		if err != nil {
			return err
		} else {
			return nil
		}
	}
}
