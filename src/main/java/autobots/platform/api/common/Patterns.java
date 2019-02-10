package autobots.platform.api.common;

public class Patterns {

    public static final String ID_NUMERICAL      = "/{id:[0-9]{1,32}}";
    public static final String ID_ALPHANUMERICAL = "/{id:[0-9a-zA-Z]{1,32}}";
    public static final String NAME              = "/{name:[0-9a-zA-Z_.\\-]{1,32}}";
    public static final String UUIDv4            = "/{uuid:[0-9a-fxA-FX]{8}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{12}}";
    public static final String UUIDv4s           = "/{uuids:[0-9a-fxA-FX]{8}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{12}(?:,[0-9a-fxA-FX]{8}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{4}-[0-9a-fxA-FX]{12})}";

}
