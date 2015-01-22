package mobi.chouette.exchange.neptune.parser;

import java.util.Date;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.neptune.Constant;
import mobi.chouette.importer.ParserUtils;
import mobi.chouette.importer.Parser;
import mobi.chouette.importer.ParserFactory;
import mobi.chouette.importer.XPPUtil;
import mobi.chouette.model.Company;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import org.xmlpull.v1.XmlPullParser;

@Log4j
public class CompanyParser implements Parser, Constant {
	private static final String CHILD_TAG = "Company";

	@Override
	public void parse(Context context) throws Exception {

		XmlPullParser xpp = (XmlPullParser) context.get(XPP);
		Referential referential = (Referential) context.get(REFERENTIAL);

		xpp.require(XmlPullParser.START_TAG, null, CHILD_TAG);
		context.put(COLUMN_NUMBER, xpp.getColumnNumber());
		context.put(LINE_NUMBER, xpp.getLineNumber());

		Company company = null;
		while (xpp.nextTag() == XmlPullParser.START_TAG) {

			if (xpp.getName().equals("objectId")) {
				String objectId = ParserUtils.getText(xpp.nextText());
				company = ObjectFactory.getCompany(referential, objectId);
			} else if (xpp.getName().equals("objectVersion")) {
				Integer version = ParserUtils.getInt(xpp.nextText());
				company.setObjectVersion(version);
			} else if (xpp.getName().equals("creationTime")) {
				Date creationTime = ParserUtils.getSQLDateTime(xpp.nextText());
				company.setCreationTime(creationTime);
			} else if (xpp.getName().equals("creatorId")) {
				company.setCreatorId(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("name")) {
				company.setName(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("shortName")) {
				company.setShortName(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("organisationalUnit")) {
				company.setOrganisationalUnit(xpp.nextText());
			} else if (xpp.getName().equals("OperatingDepartmentName")) {
				company.setOperatingDepartmentName(ParserUtils.getText(xpp
						.nextText()));
			} else if (xpp.getName().equals("organisationalUnit")) {
				company.setOrganisationalUnit(ParserUtils.getText(xpp
						.nextText()));
			} else if (xpp.getName().equals("code")) {
				company.setCode(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("phone")) {
				company.setPhone(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("fax")) {
				company.setFax(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("email")) {
				company.setEmail(ParserUtils.getText(xpp.nextText()));
			} else if (xpp.getName().equals("registration")) {

				while (xpp.nextTag() == XmlPullParser.START_TAG) {
					if (xpp.getName().equals("registrationNumber")) {
						company.setRegistrationNumber(ParserUtils.getText(xpp
								.nextText()));
					} else {
						XPPUtil.skipSubTree(log, xpp);
					}
				}
			} else {
				XPPUtil.skipSubTree(log, xpp);
			}
		}
	}

	static {
		ParserFactory.register(CompanyParser.class.getName(),
				new ParserFactory() {
					private CompanyParser instance = new CompanyParser();

					@Override
					protected Parser create() {
						return instance;
					}
				});
	}
}