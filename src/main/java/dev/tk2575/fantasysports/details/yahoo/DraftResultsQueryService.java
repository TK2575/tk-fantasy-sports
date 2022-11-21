package dev.tk2575.fantasysports.details.yahoo;

class DraftResultsQueryService extends YahooApiQueryService<DraftResults> {

    private final String leagueKey;

    DraftResultsQueryService(String leagueKey) {
        super();
        this.leagueKey = leagueKey;
    }

    @Override
    Class<DraftResults> clazz() {
        return DraftResults.class;
    }

    @Override
    String URL() {
        return "/fantasy/v2/league/%s/draftresults";
    }

    @Override
    String getQueryCode() {
        return this.leagueKey;
    }
}
