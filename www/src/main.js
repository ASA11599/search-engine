const res = document.getElementById("res");
document.getElementById("btn").onclick = (_ev) => {
    fetch("/api/index?q=" + encodeURIComponent(document.getElementById("query").value))
    .then(response => response.json())
    .then((json) => {
        document.getElementById("res").innerHTML = "";
        for (item of json) {
            let newElement = document.createElement("a");
            newElement.setAttribute("href", item["link"]);
            newElement.innerText = item["title"];
            document.getElementById("res").appendChild(newElement);
            document.getElementById("res").appendChild(document.createElement("br"));
        }
    })
    .catch((reason) => {
        res.innerText = reason;
    });
};
