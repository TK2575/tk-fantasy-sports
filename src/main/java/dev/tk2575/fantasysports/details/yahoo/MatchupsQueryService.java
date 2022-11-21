package dev.tk2575.fantasysports.details.yahoo;

class MatchupsQueryService extends YahooApiQueryService<Matchups> {

    private final String teamKey;

    MatchupsQueryService(String teamKey) {
        super();
        this.teamKey = teamKey;
    }

    @Override
    Class<Matchups> clazz() {
        return Matchups.class;
    }

    @Override
    String URL() {
        return "/fantasy/v2/league/%s/matchups";
    }

    @Override
    String getQueryCode() {
        return this.teamKey;
    }


}
