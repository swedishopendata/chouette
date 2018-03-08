package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.converter.NoptisConverter;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.Company;
import mobi.chouette.model.stip.TransportAuthority;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class NoptisTransportAuthorityParser implements Parser, Constant {

    @Getter
    @Setter
    private TransportAuthority transportAuthority;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        // TODO should consider to use gid as a postfix in objectId instead
        String objectId = NoptisConverter.composeObjectId(configuration.getObjectIdPrefix(),
                Company.COMPANY_KEY, String.valueOf(transportAuthority.getId()));
        Company company = ObjectFactory.getCompany(referential, objectId);
        company.setName(NoptisConverter.getNonEmptyTrimedString(transportAuthority.getFormalName()));

        if (company.getName() == null)
            company.setName(NoptisConverter.getNonEmptyTrimedString(transportAuthority.getName()));

        // company.setUrl(NoptisConverter.toString(transportAuthority.getUrl()));
        // company.setPhone(NoptisConverter.getNonEmptyTrimedString(transportAuthority.getPhone()));
        String[] token = company.getObjectId().split(":");
        company.setRegistrationNumber(token[2]);
        // company.setTimeZone(NoptisConverter.toString(transportAuthority.getTimezone()));
        company.setFilled(true);
    }

    static {
        ParserFactory.register(NoptisTransportAuthorityParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisTransportAuthorityParser();
            }
        });
    }

}
