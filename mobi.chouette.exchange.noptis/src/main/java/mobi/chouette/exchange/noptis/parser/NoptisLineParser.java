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
import mobi.chouette.model.Network;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import java.util.Calendar;
import java.util.Objects;

@Log4j
public class NoptisLineParser implements Parser, Constant {

    @Getter
    @Setter
    private mobi.chouette.model.stip.Line noptisLine;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        String lineId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.Line.LINE_KEY, String.valueOf(noptisLine.getGid()));
        mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);
        convert(noptisLine, neptuneLine);

        // Network
        if (referential.getSharedPTNetworks().isEmpty()) {
            Network network = createPTNetwork(referential, configuration);
            neptuneLine.setNetwork(network);
        } else {
            Network network = referential.getSharedPTNetworks().values().iterator().next();
            neptuneLine.setNetwork(network);
        }

        // Company
        if (noptisLine.getIsDefinedByTransportAuthorityId() != null) {
            String companyId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    Company.COMPANY_KEY, String.valueOf(noptisLine.getIsDefinedByTransportAuthorityId()));
            Company company = ObjectFactory.getCompany(referential, companyId);
            neptuneLine.setCompany(company);
        } else if (!referential.getSharedCompanies().isEmpty()) {
            Company company = referential.getSharedCompanies().values().iterator().next();
            neptuneLine.setCompany(company);
        }
    }

    private void convert(mobi.chouette.model.stip.Line noptisLine, mobi.chouette.model.Line neptuneLine) {
        neptuneLine.setName(AbstractNoptisParser.getNonEmptyTrimedString(noptisLine.getName()));

        if (neptuneLine.getName() == null) {
            neptuneLine.setName(AbstractNoptisParser.getNonEmptyTrimedString(noptisLine.getDesignation()));
        }

        neptuneLine.setNumber(AbstractNoptisParser.getNonEmptyTrimedString(noptisLine.getDesignation()));
        neptuneLine.setPublishedName(AbstractNoptisParser.getNonEmptyTrimedString(noptisLine.getName()));

        if (neptuneLine.getPublishedName() != null) {
            neptuneLine.setName(neptuneLine.getPublishedName());
        } else {
            neptuneLine.setName(neptuneLine.getNumber());
        }

        neptuneLine.setTransportModeName(AbstractNoptisParser.convertTransportModeCode(noptisLine.getDefaultTransportModeCode()));

        String[] token = neptuneLine.getObjectId().split(":");
        neptuneLine.setRegistrationNumber(token[2]);

        //neptuneLine.setComment(noptisLine.getDescription());
        //neptuneLine.setColor(toHexa(noptisLine.getLineColor()));
        //neptuneLine.setTextColor(toHexa(noptisLine.getLineTextColor()));
        //neptuneLine.setUrl(NoptisConverter.toString(noptisLine.getLineUrl()));

        neptuneLine.setFilled(true);
    }

    private Network createPTNetwork(Referential referential, NoptisImportParameters configuration) {
        String objectIdPrefix = configuration.getObjectIdPrefix();
        String ptNetworkId = AbstractNoptisParser.composeObjectId(objectIdPrefix, Network.PTNETWORK_KEY, objectIdPrefix);
        Network ptNetwork = ObjectFactory.getPTNetwork(referential, ptNetworkId);
        ptNetwork.setVersionDate(Calendar.getInstance().getTime());
        ptNetwork.setName(objectIdPrefix);
        ptNetwork.setRegistrationNumber(objectIdPrefix);
        ptNetwork.setSourceName("NOPTIS");
        return ptNetwork;
    }

    static {
        ParserFactory.register(NoptisLineParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisLineParser();
            }
        });
    }

}
