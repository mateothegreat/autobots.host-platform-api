package autobots.platform.api.bots;

public enum BotStatus {

    PENDING("PENDING"), DEPLOYING("DEPLOYING"), DEPLOYED("DEPLOYED");

    private final String status;

    BotStatus(final String status) {

        this.status = status;

    }

}
