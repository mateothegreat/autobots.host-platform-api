package autobots.platform.api.bots;

public enum BotImage {

    NODE_11_9_0_ALPINE("NODE_11_9_0_ALPINE"), NODE_8_15_0_ALPINE("NODE_8_15_0_ALPINE"), PYTHON_3_7_2_ALPINE("PYTHON_3_7_2_ALPINE"), PYTHON_2_7_15_ALPINE("PYTHON_2_7_15_ALPINE");

    private final String image;

    BotImage(final String image) {

        this.image = image;

    }


}
