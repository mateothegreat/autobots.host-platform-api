package autobots.platform.api.common;

public enum Status {

    ACTIVE(0), LOCKED(1), SUSPENDED(2), PENDING(3), DISABLED(4), DELETED(5), CANCELLED(6);

    private final int status;

    public static Status getStatusFromName(String name) {

        if (name.equals("ACTIVE")) {

            return Status.ACTIVE;

        } else if (name.equals("LOCKED")) {

            return Status.LOCKED;

        } else if (name.equals("SUSPENDED")) {

            return Status.SUSPENDED;

        } else if (name.equals("DELETED")) {

            return Status.DELETED;

        } else if (name.equals("DISABLED")) {

            return Status.DISABLED;

        }

        return null;

    }

    Status(final int status) {

        this.status = status;

    }

    public int getValue() {

        return status;

    }

}
