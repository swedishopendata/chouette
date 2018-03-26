package mobi.chouette.exchange.noptis.parser;

import mobi.chouette.model.stip.CallOnTimedJourneyPattern;
import mobi.chouette.model.stip.PointInJourneyPattern;

public class CallAndPointInJourneyPattern {

    private final CallOnTimedJourneyPattern callOnTimedJourneyPattern;
    private final PointInJourneyPattern pointInJourneyPattern;

    public CallAndPointInJourneyPattern(CallOnTimedJourneyPattern callOnTimedJourneyPattern, PointInJourneyPattern pointInJourneyPattern) {
        this.callOnTimedJourneyPattern = callOnTimedJourneyPattern;
        this.pointInJourneyPattern = pointInJourneyPattern;
    }

    public CallOnTimedJourneyPattern getCallOnTimedJourneyPattern() {
        return callOnTimedJourneyPattern;
    }

    public PointInJourneyPattern getPointInJourneyPattern() {
        return pointInJourneyPattern;
    }
}
