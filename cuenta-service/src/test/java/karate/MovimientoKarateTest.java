package karate;

import com.intuit.karate.junit5.Karate;

class MovimientoKarateTest {

    @Karate.Test
    Karate testMovimientos() {
        return Karate.run("movimientos").relativeTo(getClass());
    }
}
