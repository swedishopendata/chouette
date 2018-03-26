package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.type.ConnectionLinkTypeEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import java.sql.Time;
import java.util.Calendar;

@Log4j
public class NoptisConnectionLinkParser implements Parser, Constant {

    @Getter
    @Setter
    private mobi.chouette.model.stip.ConnectionLink noptisConnectionLink;

    @Getter
    @Setter
    private mobi.chouette.model.stip.StopPoint fromStopPoint;

    @Getter
    @Setter
    private mobi.chouette.model.stip.StopPoint toStopPoint;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.ConnectionLink.CONNECTIONLINK_KEY, fromStopPoint.getGid() + "_" + toStopPoint.getGid());
        mobi.chouette.model.ConnectionLink neptuneConnectionLink = ObjectFactory.getConnectionLink(referential, objectId);

        StopArea startOfLink = ObjectFactory.getStopArea(referential, AbstractNoptisParser.composeObjectId(
                configuration.getObjectIdPrefix(), StopArea.STOPAREA_KEY, String.valueOf(fromStopPoint.getGid())));
        neptuneConnectionLink.setStartOfLink(startOfLink);

        StopArea endOfLink = ObjectFactory.getStopArea(referential, AbstractNoptisParser.composeObjectId(
                configuration.getObjectIdPrefix(), StopArea.STOPAREA_KEY, String.valueOf(toStopPoint.getGid())));
        neptuneConnectionLink.setEndOfLink(endOfLink);

        neptuneConnectionLink.setCreationTime(Calendar.getInstance().getTime());
        neptuneConnectionLink.setLinkType(ConnectionLinkTypeEnum.Overground);
        neptuneConnectionLink.setDefaultDuration(new Time(noptisConnectionLink.getDefaultDurationSeconds() * 1000));

        neptuneConnectionLink.setName("from " + neptuneConnectionLink.getStartOfLink().getName()
                + " to " + neptuneConnectionLink.getEndOfLink().getName());

        neptuneConnectionLink.setFilled(true);
    }

    static {
        ParserFactory.register(NoptisConnectionLinkParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisConnectionLinkParser();
            }
        });
    }

}
