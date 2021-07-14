#!/usr/bin/env bash

if ! command -v curl &> /dev/null
then
    echo "curl could not be found"
    exit
fi

if ! command -v jq &> /dev/null
then
    echo "jq could not be found"
    exit
fi

server=http://localhost:8080

until curl -s --output /dev/null $server; do
  echo 'waiting for api...'
  sleep 3
done

echo "Listing all devices ..."
curl -s "$server/devices" | jq '.';

echo "Adding new device ..."
curl -s -H "Content-Type: application/json" -XPOST  -d '{"name": "new_device","brand": "brand2","creationDateTime": "2005-05-05T10:10:10Z"}' "$server/devices";

last_id=$(curl -s "$server/devices" | jq '.[-1].id');

echo "Get new device ..."
curl -s "$server/devices/${last_id}" | jq '.'

echo "Update device name and brand ..."
curl -s -H "Content-Type: application/json" -XPUT  -d '{"name": "updated_device_name","brand":"brand3"}' "$server/devices/${last_id}";

echo "Get updated device ..."
curl -s "$server/devices/${last_id}" | jq '.'

echo "Delete device  ..."
curl -s -H "Content-Type: application/json" -XDELETE "$server/devices/${last_id}";

echo "Listing all devices ..."
curl -s "$server/devices" | jq '.';