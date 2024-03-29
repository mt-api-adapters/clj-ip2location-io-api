
(ns ip2location-io.engine
    (:require [clj-http.client]
              [data-audit.api          :as data-audit]
              [fruits.json.api         :as json]
              [fruits.map.api          :as map]
              [fruits.reader.api       :as reader]
              [ip2location-io.messages :as messages]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn locate-ip-address
  ; @description
  ; Locates the given IP address using the [ip2location.io](https://ip2location.io) API.
  ;
  ; @param (string) ip-address
  ; @param (string) api-key
  ;
  ; @usage
  ; (locate-ip-address "x.x.x.x" "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
  ; =>
  ; {:as           "Go P.L.C."
  ;  :asn          "15735"
  ;  :city-name    "Sliema"
  ;  :country-name "Malta"
  ;  :country-code "MT"
  ;  :ip           "195.158.84.203"
  ;  :is-proxy?    false
  ;  :latitude     "35.9125"
  ;  :longitude    "14.50194"
  ;  :region-name  "Sliema"
  ;  :time-zone    "+02:00"
  ;  :zip-code     "SLM 3180"}
  ;
  ; @return (map)
  ; {:as (string)
  ;   Autonomous system (AS) name.
  ;  :asn (string)
  ;   Autonomous system number (ASN).
  ;  :city-name (string)
  ;   City name.
  ;  :country-code (string)
  ;   Two-character country code based on ISO 3166.
  ;  :country-name (string)
  ;   Country name based on ISO 3166.
  ;  :ip (string)
  ;   IP address.
  ;  :isp (string)
  ;   Internet Service Provider or company's name.
  ;  :is-proxy? (boolean)
  ;  :latitude (string)
  ;   City latitude. Defaults to capital city latitude if city is unknown.
  ;  :longitude (string)
  ;   City longitude. Defaults to capital city longitude if city is unknown.
  ;  :region-name (string)
  ;   Region or state name.
  ;  :time-zone (string)
  ;   UTC time zone (with DST supported).
  ;  :zip-code (string)
  ;   ZIP/Postal code.}
  [ip-address api-key]
  (if (data-audit/ip-address-valid? ip-address)
      (-> (str "http://api.ip2location.io/?ip="ip-address"&key="api-key)
          (clj-http.client/get)
          (:body)
          (reader/parse-json)
          (json/hyphenize-keys)
          (json/keywordize-keys)
          (map/move :is-proxy :is-proxy?))
      (throw (Exception. messages/INVALID-IP-ADDRESS-ERROR))))
