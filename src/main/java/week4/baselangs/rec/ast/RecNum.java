package week4.baselangs.rec.ast;

// See klass on kirje (record), mistõttu konstruktor, get meetodid (ilma get eesliiteta), equals, hashCode ja toString genereeritakse automaatselt kompilaatori poolt.
// See on võimalik alates Java 16-st.
public record RecNum(int value) implements RecNode {
}
