package models

import (
	"crypto/md5"
	"database/sql"
	"encoding/base64"
	"encoding/hex"
	"errors"
	"io"
	"strconv"
	"strings"
)

type Admin struct {
	Username string
	Password string
}

func newAdmin(username string, password string) *Admin {
	return &Admin{Username: username, Password: password}
}

func AdminFromBase64String(base64String string) (*Admin, error){
	bytes, err := base64.StdEncoding.DecodeString(base64String)
	if err != nil {
		return nil, err
	} else {
		str := string(bytes)
		res := strings.Split(str, ":")
		if len(res) != 2 {
			return nil, errors.New("Username and password must be separated by a single colon character")
		} else {
			return newAdmin(res[0], res[1]), nil
		}
	}
}

func (this *Admin) getPasswordHash() (string, error) {
	h := md5.New()
	_, err := io.WriteString(h, this.Password)
	res := hex.EncodeToString(h.Sum(nil))
	return res, err
}

func (this *Admin) Authenticate() (bool, error) {
	db, err := sql.Open("pq", "host=" + dbhost + " port=" + strconv.Itoa(dbport) + " user=" + dbuser + " password=" + password + " dbname=" + dbname + " sslmode=disable")
	if err != nil {
		return false, err
	} else {
		defer db.Close()
		rows, err := db.Query("SELECT PasswordHash FROM Administrators WHERE Username='" + this.Username + "';")
		if err != nil {
			return false, err
		} else {
			for rows.Next() {
				var dbPasswordHash string
				err := rows.Scan(&dbPasswordHash)
				if err != nil {
					return false, err
				} else {
					thisPasswordHash, err := this.getPasswordHash()
					if err != nil {
						return false, err
					} else {
						return thisPasswordHash == dbPasswordHash, nil
					}
				}
			}
			return false, nil
		}
	}
}
