package dev.tk2575.fantasysports.details.yahoo;

class DraftResultsQueryService extends YahooApiQueryService<DraftResults> {

    @Override
    Class<DraftResults> clazz() {
        return DraftResults.class;
    }

    @Override
    String URL() {
        return "/fantasy/v2/league/%s/draftresults";
    }
}
