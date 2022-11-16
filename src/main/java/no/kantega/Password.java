package no.kantega;

public final class Password {
    public final int id;
    public final String hashedPassword;
    public final String cleartextPassword;
    public final String status;

    public Password(int id, String hashedPassword, String cleartextPassword, String status) {
        this.id = id;
        this.hashedPassword = hashedPassword;
        this.cleartextPassword = cleartextPassword;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getCleartextPassword() {
        return cleartextPassword;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Password{" +
               "id=" + id +
               ", hashedPassword='" + hashedPassword + '\'' +
               ", cleartextPassword='" + cleartextPassword + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
