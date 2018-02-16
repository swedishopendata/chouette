package mobi.chouette.exchange.noptis.converter;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.Converter;
import mobi.chouette.exchange.noptis.importer.ConverterFactory;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.Company;
import mobi.chouette.model.stip.TransportAuthority;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

@Log4j
public class NoptisTransportAuthorityConverter extends NoptisConverter implements Converter, Constant {

    @Override
    public void convert(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
        TransportAuthority transportAuthority = (TransportAuthority) context.get(NOPTIS_DATA_CONTEXT);
        log.info(transportAuthority.toString());

        String objectId = NoptisConverter.composeObjectId(configuration.getObjectIdPrefix(), Company.COMPANY_KEY, String.valueOf(transportAuthority.getGid()));
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
        ConverterFactory.register(NoptisTransportAuthorityConverter.class.getName(), new ConverterFactory() {
            private NoptisTransportAuthorityConverter instance = new NoptisTransportAuthorityConverter();

            @Override
            protected Converter create() {
                return instance;
            }
        });
    }

}
