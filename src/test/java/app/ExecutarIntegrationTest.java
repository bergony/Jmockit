package app;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class ExecutarIntegrationTest {

    @Injectable
    public Colaborador colaborador;

    @Tested
    public Executar executar;

    @Test
    public void testThePerformMethod(@Mocked Modelo modelo) {
        new Expectations() {{
            modelo.getInfo();result = "bar";
            colaborador.colaborar("bar");
            result = true;
        }};

        executar.execute(modelo);

        new Verifications() {{
            colaborador.receber(true);
        }};
    }

}
