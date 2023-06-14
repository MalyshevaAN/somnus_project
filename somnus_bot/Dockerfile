FROM python:3.10
WORKDIR /app
COPY requirements.txt requirements.txt
RUN pip3 install --upgrade setuptools
RUN pip3 install -r requirements.txt
RUN pip3 install -U --pre aiogram==3.0.0b6
RUN chmod 755 .
COPY . .