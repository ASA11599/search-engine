package main

import (
	"api/models"
	"database/sql"
	"encoding/json"
	"io/ioutil"
	"net/http"
	"strings"

	"github.com/lib/pq"
)

func main() {

	sql.Register("pq", pq.Driver{})

	var routes map[string]func(http.ResponseWriter, *http.Request) = map[string]func(http.ResponseWriter, *http.Request){

		"/": func (rw http.ResponseWriter, r *http.Request) {
			rw.Header().Set("Content-Type", "application/json")
			rw.WriteHeader(http.StatusNotFound)
			rw.Write([]byte("{\"error\": \"Resource not found\"}"))
		},

		"/index": func (rw http.ResponseWriter, r *http.Request) {
			rw.Header().Set("Content-Type", "application/json")
			if r.Method == http.MethodGet {
				q := r.URL.Query().Get("q")
				pages, err := models.Query(q)
				if err != nil {
					rw.WriteHeader(http.StatusInternalServerError)
					rw.Write([]byte("{\"error\": \"Unable to query the index\", \"message\": \"" + err.Error() + "\"}"))
				} else {
					pagesJson, err := json.Marshal(pages)
					if err != nil {
						rw.WriteHeader(http.StatusInternalServerError)
						rw.Write([]byte("{\"error\": \"Internal server error\", \"message\": \"" + err.Error() + "\"}"))
					} else {
						rw.WriteHeader(http.StatusOK)
						rw.Write(pagesJson)
					}
				}
			} else if r.Method == http.MethodPost {
				contentType := r.Header.Get("Content-Type")
				if (contentType != "application/json") {
					rw.WriteHeader(http.StatusBadRequest)
					rw.Write([]byte("{\"error\": \"Invalid Content-Type, must be application/json\"}"))
				} else {
					auth := strings.Split(r.Header.Get("Authorization"), " ")
					if (len(auth) != 2) || (auth[0] != "Basic") {
						rw.WriteHeader(http.StatusUnauthorized)
						rw.Write([]byte("{\"error\": \"Unauthorized\"}"))
					} else {
						admin, err := models.AdminFromBase64String(auth[1])
						if err != nil {
							rw.WriteHeader(http.StatusBadRequest)
							rw.Write([]byte("{\"error\": \"Unable to decode credentials\", \"message\": \"" + err.Error() + "\"}"))
						} else {
							success, err := admin.Authenticate()
							if err != nil {
								rw.WriteHeader(http.StatusInternalServerError)
								rw.Write([]byte("{\"error\": \"Unable to authenticate\", \"message\": \"" + err.Error() + "\"}"))
							} else {
								if success {
									bytes, err := ioutil.ReadAll(r.Body)
									if err != nil {
										rw.WriteHeader(http.StatusInternalServerError)
										rw.Write([]byte("{\"error\": \"Unable to read request body\", \"message\": \"" + err.Error() + "\"}"))
									} else {
										webPage, err := models.WebPageFromJSON(string(bytes))
										if err != nil {
											rw.WriteHeader(http.StatusBadRequest)
											rw.Write([]byte("{\"error\": \"Unable to parse request body\", \"message\": \"" + err.Error() + "\"}"))
										} else {
											err := webPage.AddToIndex()
											if err != nil {
												rw.WriteHeader(http.StatusInternalServerError)
												rw.Write([]byte("{\"error\": \"Unable to add page to index\", \"message\": \"" + err.Error() + "\"}"))
											} else {
												json, err := webPage.ToJSON()
												if err != nil {
													rw.WriteHeader(http.StatusCreated)
													rw.Write([]byte("{\"message\": \"Page added to index\"}"))
												} else {
													rw.WriteHeader(http.StatusCreated)
													rw.Write([]byte(json))
												}
											}
										}
									}
								} else {
									rw.WriteHeader(http.StatusForbidden)
									rw.Write([]byte("{\"error\": \"Forbidden\"}"))
								}
							}
						}
					}
				}
			} else if r.Method == http.MethodHead {
				rw.WriteHeader(http.StatusMethodNotAllowed)
				rw.Header().Del("Content-Type")
				rw.Header().Del("Content-Length")
			} else {
				rw.WriteHeader(http.StatusMethodNotAllowed)
				rw.Write([]byte("{\"error\": \"Invalid method, must be POST or GET\"}"))
			}
		},

	}

	for path, handler := range routes {
		http.HandleFunc(path, handler)
	}

	http.ListenAndServe("0.0.0.0:80", nil)

}
