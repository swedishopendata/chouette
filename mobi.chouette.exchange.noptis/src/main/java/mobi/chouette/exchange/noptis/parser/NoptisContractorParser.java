package mobi.chouette.exchange.noptis.parser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.Company;
import mobi.chouette.model.stip.Contractor;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Log4j
public class NoptisContractorParser implements Parser, Constant {

    @Getter
    @Setter
    private List<Contractor> contractors;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        for (Contractor contractor : contractors) {
            String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    Company.COMPANY_KEY, String.valueOf(contractor.getGid()));

            Company contractorCompany = ObjectFactory.getCompany(referential, objectId);

            String contractorCode = contractor.getCode();
            if (StringUtils.isNotEmpty(contractorCode)) {
                contractorCompany.setCode(contractorCode);
                contractorCompany.setShortName(contractorCode);
            }
            if (StringUtils.isNotEmpty(contractor.getName())) {
                contractorCompany.setName(contractor.getName());
            }

            String transportAuthorityObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    Company.COMPANY_KEY, String.valueOf(contractor.getIsPromotedByTransportAuhthorityId()));
            Company operatingDepartmentCompany = ObjectFactory.getCompany(referential, transportAuthorityObjectId);
            contractorCompany.setOperatingDepartmentName(operatingDepartmentCompany.getName());

            // company.setUrl(NoptisConverter.toString(transportAuthority.getUrl()));
            // company.setPhone(NoptisConverter.getNonEmptyTrimedString(transportAuthority.getPhone()));
            // String[] token = company.getObjectId().split(":");
            // company.setRegistrationNumber(token[2]);
            // company.setTimeZone(NoptisConverter.toString(transportAuthority.getTimezone()));

            contractorCompany.setFilled(true);
        }
    }

    static {
        ParserFactory.register(NoptisContractorParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisContractorParser();
            }
        });
    }

}
