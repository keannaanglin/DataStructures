package sentinal;

public class PhraseHash implements PhraseHashInterface {

    // -----------------------------------------------------------
    // Fields
    // -----------------------------------------------------------

    private final static int BUCKET_START = 1000;
    private final static double LOAD_MAX = 0.7;
    private int size, longest;
    private String[] buckets;


    // -----------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------

    PhraseHash () {
        // TODO: Populate fields as appropriate
        // [!] Don't forget to dynamically allocate buckets!
    }


    // -----------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------

    public int size () {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty () {
        throw new UnsupportedOperationException();
    }

    public void put (String s) {
        throw new UnsupportedOperationException();
    }

    public String get (String s) {
        throw new UnsupportedOperationException();
    }

    public int longestLength () {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------

    private int hash (String s) {
        throw new UnsupportedOperationException();
    }

    private void checkAndGrow () {
        throw new UnsupportedOperationException();
    }

}
