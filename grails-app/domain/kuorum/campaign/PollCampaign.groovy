package kuorum.campaign

class PollCampaign extends Campaign{

    String name;
    List<String> values;
    Map<String, Long> results;

    static constraints = {
    }
}
