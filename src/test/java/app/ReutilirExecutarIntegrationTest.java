package app;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class ReutilirExecutarIntegrationTest {
    @Injectable
    public Colaborador collaborator;

    @Mocked
    public Modelo modelo;

    @Tested
    public Executar executar;


    @Before
    public void setup(){
        new Expectations(){{
            modelo.getInfo(); result = "foo"; minTimes = 0;
            collaborator.colaborar("foo"); result = true; minTimes = 0;
        }};
    }

    @Test
    public void testWithSetup() {
        executar.execute(modelo);
        verifyTrueCalls(1);
    }

    protected void verifyTrueCalls(int calls){
        new Verifications(){{
            collaborator.receber(true); times = calls;
        }};
    }

    final class TrueCallsVerification extends Verifications{
        public TrueCallsVerification(int calls){
            collaborator.receber(true); times = calls;
        }
    }

    @Test
    public void testWithFinalClass() {
        executar.execute(modelo);
        new TrueCallsVerification(1);
    }

}
