package MongoCollections;

public enum Users
{
    Collection("users"),
    PhoneNumber("phoneNumber"),
    Token("token"),
    ContactList("contactList"),
    VirtualAgent("VirtualAgent")
    ;

    private final String text;

    /**
     * @param text
     */
    Users(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
