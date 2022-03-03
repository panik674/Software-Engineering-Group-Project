package uk.comp2211.group13.data;

import java.util.Iterator;

public class Metrics {

    private Data data = Data.retreiv

    public float clickTrough(Search search) {
        Data data = Data.retreive(search);
        int sum = 0;
        Iterator a = data.iterator();
        while (a.hasNext()) {
            sum += a.next().getClicks();
        }
        return (sum / clicks.size());
    }

    public float bounceRate(Search search) {
        Data data = Data.retreive(search);
        int clicks = data.getClicks();
        int bounce = data.getBounce();
        return (bounce / clicks);
    }

    public float conversionRate(Search search) {
        Data data = Data.retreive(search);
        int conversions = data.getConversions();
        int clicks = data.getClicks();
        return conversions / clicks;
    }

    public float costAcquisition(Search search) {
        Data data = Data.retreive(search);

        public float costClick (Search search){
            Data data = Data.retreive(search);
            int cost = data.getCost();
            int clicks = data.getClicks();
            return (cost / clicks);
        }

        public float cpm (Search search){
            Data data = Data.retreive(search);
            int cost = data.getCost();
            int impressions = data.getImpressions();
            return cost / impressions * 1000;
        }

    }
}