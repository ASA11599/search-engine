FROM python:3

WORKDIR /app

COPY ./__main__.py .
COPY ./utils.py .
COPY ./requirements.txt .

RUN pip3 install -r requirements.txt

CMD ["python3", "."]
