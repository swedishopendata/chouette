package mobi.chouette.exchange.neptune.parser;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.neptune.Constant;
import mobi.chouette.importer.ParserUtils;
import mobi.chouette.importer.Parser;
import mobi.chouette.importer.ParserFactory;
import mobi.chouette.importer.XPPUtil;
import mobi.chouette.model.ConnectionLink;
import mobi.chouette.model.StopArea;
import mobi.chouette.model.type.ConnectionLinkTypeEnum;
import mobi.chouette.model.type.UserNeedEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import org.xmlpull.v1.XmlPullParser;

@Log4j
public class ConnectionLinkParser implements Parser, Constant {
	private static final String CHILD_TAG = "ConnectionLink";

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(XPP);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, CHILD_TAG);
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());
		
		ConnectionLink connectionLink = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {

			if (xpp.getName().equals("objectId")) {
				String objectId = ParserUtils.getText(xpp.nextText());
				connectionLink = ObjectFactory.getConnectionLink(referential,
						objectId);
			} else if (xpp.getName().equals("objectVersion")) {
				Integer version = ParserUtils.getInt(xpp.nextText());
				connectionLink.setObjectVersion(version);
			} else if (xpp.getName().equals("creationTime")) {
				Date creationTime = ParserUtils.getSQLDateTime(xpp.nextText());
				connectionLink.setCreationTime(creationTime);
			} else if (xpp.getName().equals("creatorId")) {
				connectionLink
						.setCreatorId(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("name")) {
				connectionLink.setName(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("comment")) {
				connectionLink.setComment(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("startOfLink")) {
				String objectId = ParserUtils.getText(xpp.nextText());
				StopArea startOfLink = ObjectFactory.getStopArea(referential,
						objectId);
				connectionLink.setStartOfLink(startOfLink);
			} else if (xpp.getName().equals("endOfLink")) {
				String objectId = ParserUtils.getText(xpp.nextText());
				StopArea endOfLink = ObjectFactory.getStopArea(referential,
						objectId);
				connectionLink.setStartOfLink(endOfLink);
			} else if (xpp.getName().equals("linkDistance")) {
				BigDecimal value = ParserUtils.getBigDecimal(xpp.nextText());
				connectionLink.setLinkDistance(value);

			} else if (xpp.getName().equals("liftAvailability")) {
				boolean value = ParserUtils.getBoolean(xpp.nextText());
				connectionLink.setLiftAvailable(value);
			} else if (xpp.getName().equals("mobilityRestrictedSuitability")) {
				boolean value = ParserUtils.getBoolean(xpp.nextText());
				connectionLink.setMobilityRestrictedSuitable(value);
			} else if (xpp.getName().equals("stairsAvailability")) {
				boolean value = ParserUtils.getBoolean(xpp.nextText());
				connectionLink.setStairsAvailable(value);
			} else if (xpp.getName().equals("ConnectionLinkExtension")) {
				while (xpp.nextTag() == XmlPullParser.START_TAG) {

					if (xpp.getName().equals("accessibilitySuitabilityDetails")) {
						List<UserNeedEnum> userNeeds = new ArrayList<UserNeedEnum>();
						while (xpp.nextTag() == XmlPullParser.START_TAG) {
							if (xpp.getName().equals("MobilityNeed")
									|| xpp.getName()
											.equals("PsychosensoryNeed")
									|| xpp.getName().equals("MedicalNeed")) {
								UserNeedEnum userNeed = ParserUtils.getEnum(
										UserNeedEnum.class, xpp.nextText());
								if (userNeed != null) {
									userNeeds.add(userNeed);
								}
							} else {
								XPPUtil.skipSubTree(log, xpp);
							}
						}
						connectionLink.setUserNeeds(userNeeds);
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else if (xpp.getName().equals("defaultDuration")) {
				Time value = ParserUtils.getSQLDuration(xpp.nextText());
				connectionLink.setDefaultDuration(value);
			} else if (xpp.getName().equals("frequentTravellerDuration")) {
				Time value = ParserUtils.getSQLDuration(xpp.nextText());
				connectionLink.setFrequentTravellerDuration(value);
			} else if (xpp.getName().equals("occasionalTravellerDuration")) {
				Time value = ParserUtils.getSQLDuration(xpp.nextText());
				connectionLink.setOccasionalTravellerDuration(value);
			} else if (xpp.getName().equals(
					"mobilityRestrictedTravellerDuration")) {
				Time value = ParserUtils.getSQLDuration(xpp.nextText());
				connectionLink.setMobilityRestrictedTravellerDuration(value);
			} else if (xpp.getName().equals("linkType")) {
				ConnectionLinkTypeEnum value = ParserUtils.getEnum(
						ConnectionLinkTypeEnum.class, xpp.nextText());
				connectionLink.setLinkType(value);
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	static {
		ParserFactory.register(ConnectionLinkParser.class.getName(),
				new ParserFactory() {
					private ConnectionLinkParser instance = new ConnectionLinkParser();

					@Override
					protected Parser create() {
						return instance;
					}
				});
	}
}
