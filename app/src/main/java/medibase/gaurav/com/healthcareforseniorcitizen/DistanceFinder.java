package medibase.gaurav.com.healthcareforseniorcitizen;
import java.lang.*;

    class DistanceFinder
    {
        double lat1[],lon1[],lats,lons;
        long pnumber[];

        public DistanceFinder(){}

        public DistanceFinder(double lat[],double lon[],double dlat,double dlon, long num[])
        {
            lat1 = new double[lat.length];
            lon1 = new double[lon.length];
            pnumber = new long[lon.length];
            lats = dlat;
            lons = dlon;

            for(int i = 0; i<lat1.length; i++)
            {
                lat1[i] = lat[i];
                lon1[i] = lon[i];
                pnumber[i] = num[i];
            }
//            rangeOfDistance(lat1,lon1,lats,lons);
        }


        private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }

            return (dist);
        }

        private static double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private static double rad2deg(double rad) {
            return (rad * 180 / Math.PI);
        }

        public long[] rangeOfDistance(double lats,double lons)
        {
            long pos[] = new long[10];
            int k = 0;
            for(int i = 0 ; i < lat1.length ; i++)
            {
                double dist = distance(lat1[i],lats,lon1[i],lons,"K");
                if(dist<=1880.0)
                {
                    pos[k++] = pnumber[i];
                }
            }
            /*System.out.println(k);
            for(int i = 0; i<k ; i++)
            {
                System.out.println("Indexes = "+pos[i]);
            }*/
            return pos;
        }

    }

