FROM node:boron
RUN apt-get update -qq && apt-get install -y build-essential libpq-dev libkrb5-dev
RUN mkdir /geolocation
WORKDIR /geolocation
ADD package.json /geolocation/package.json
RUN npm install
ADD . /geolocation