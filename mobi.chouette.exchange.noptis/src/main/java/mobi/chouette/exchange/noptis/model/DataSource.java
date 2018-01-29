package mobi.chouette.exchange.noptis.model;

public interface DataSource {
    short getId();

    String getName();

    String getDoiDbUser();

    String getDoiDbPassword();

    String getDoiDbUrl();
}
