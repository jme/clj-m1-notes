(defn dist-havorsine
  "Caclulate geospatial distance using the Havorsine algorithm

  units:: input coordinates: degrees
          output value:  meters"

  [^Double lat1, ^Double lng1, ^Double lat2, ^Double lng2]

  (let [earth-radius-km 6371.0
        rad-factor (/ (. Math PI) 180.0)

        from-lat (* lat1 rad-factor)
        from-lng (* lng1 rad-factor)

        to-lat (* lat2 rad-factor)
        to-lng (* lng2 rad-factor)

        dlat (- to-lat from-lat) 
        dlng (- to-lng from-lng)

        hdlat (/ dlat 2.0) 
        hdlng (/ dlng 2.0)

        aterm (. Math sin hdlat)
        bterm (. Math sin hdlng)

        s  ( *  aterm  aterm)
        s1 ( * (. Math cos from-lat) (. Math cos to-lat) bterm  bterm ) 

        sx (+ s s1) 
        d ( * 2.0 (. Math atan2 (. Math sqrt sx) (. Math sqrt (- 1 sx))))]

    (* earth-radius-km  d  1000.0)))


