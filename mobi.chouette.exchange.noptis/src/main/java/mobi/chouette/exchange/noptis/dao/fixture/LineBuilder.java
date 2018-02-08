package mobi.chouette.exchange.noptis.dao.fixture;

import mobi.chouette.exchange.noptis.model.Line;
import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import java.time.LocalDate;

public class LineBuilder {

    private Long id;
    private short number;
    private String name;
    private String designation;
    private TransportModeCode defaultTransportModeCode;
    private Long isDefinedByTransportAuthorityId;
    private boolean monitored;
    private LocalDate existsFromDate;
    private LocalDate existsUpToDate;

    public static LineBuilder newInstance() {
        return new LineBuilder();
    }

    public Line build() {
        Line line = new Line();
        line.setId(this.id);
        line.setNumber(this.number);
        line.setName(this.name);
        line.setDesignation(this.designation);
        line.setDefaultTransportModeCode(this.defaultTransportModeCode);
        line.setIsDefinedByTransportAuthorityId(isDefinedByTransportAuthorityId);
        line.setMonitored(this.monitored);
        line.setExistsFromDate(this.existsFromDate);
        line.setExistsUpToDate(this.existsUpToDate);
        return line;
    }

    public LineBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LineBuilder withNumber(short number) {
        this.number = number;
        return this;
    }

    public LineBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LineBuilder withDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public LineBuilder withTransportModeCode(TransportModeCode defaultTransportModeCode) {
        this.defaultTransportModeCode = defaultTransportModeCode;
        return this;
    }

    public LineBuilder withIsDefinedByTransportAuthorityId(Long isDefinedByTransportAuthorityId) {
        this.isDefinedByTransportAuthorityId = isDefinedByTransportAuthorityId;
        return this;
    }

    public LineBuilder withMonitored(boolean monitored) {
        this.monitored = monitored;
        return this;
    }

    public LineBuilder withExistsFromDate(LocalDate existsFromDate) {
        this.existsFromDate = existsFromDate;
        return this;
    }

    public LineBuilder withExistsUpToDate(LocalDate existsUpToDate) {
        this.existsUpToDate = existsUpToDate;
        return this;
    }

}
