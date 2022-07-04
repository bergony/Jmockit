package app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mockit.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class ExecutarIntegrationTest {

    @Injectable
    public Colaborador colaborador;

    @Tested
    public Executar executar;

    @Test
    public void testExecutarMethod(@Mocked Modelo modelo) {
        //O comportamento esperado para mocks
        new Expectations() {{
            modelo.getInfo();
            result = "bar";
            colaborador.colaborar("bar");
            result = true;
        }};

        executar.execute(modelo);

        //verificar  mocks
        new Verifications() {{
            colaborador.receber(true);
        }};
    }

    @Test
    public void test(@Mocked ExpectativaCollaborator mock) throws Exception {
        new Expectations() {{
            mock.methodForAny1(anyString, anyInt, anyBoolean);
            result = "any";
        }};

        assertEquals("any", mock.methodForAny1("barfooxyz", 0, Boolean.FALSE));
        mock.methodForAny2(2L, new ArrayList<>());

        new FullVerifications() {{
            mock.methodForAny2(anyLong, (List<String>) any);
        }};
    }

    @Test
    public void testForWith(@Mocked ExpectativaCollaborator mock) throws Exception {
        new Expectations() {{
            mock.methodForWith1(withSubstring("foo"), withNotEqual(1));
            result = "with";
        }};

        assertEquals("with", mock.methodForWith1("barfooxyz", 2));
        mock.methodForWith2(Boolean.TRUE, new ArrayList<>());

        new Verifications() {{
            mock.methodForWith2(withNotNull(), withInstanceOf(List.class));
        }};
    }

    @Test
    public void testWithNulls(@Mocked ExpectativaCollaborator mock) {
        new Expectations() {{
            mock.methodForNulls1(anyString, null);
            result = "null";
        }};

        assertEquals("null", mock.methodForNulls1("blablabla", new ArrayList<String>()));
        mock.methodForNulls2("blablabla", null);

        new Verifications() {{
            mock.methodForNulls2(anyString, (List<String>) withNull());
        }};
    }

    @Test
    public void testWithTimes(@Mocked ExpectativaCollaborator mock) {
        new Expectations() {{
            mock.methodForTimes1();
            times = 2;
            mock.methodForTimes2();
        }};

        mock.methodForTimes1();
        mock.methodForTimes1();
        mock.methodForTimes2();
        mock.methodForTimes3();
        mock.methodForTimes3();
        mock.methodForTimes3();

        new Verifications() {{
            mock.methodForTimes3();
            minTimes = 1;
            maxTimes = 3;
        }};
    }

    @Test
    public void testCustomArgumentMatching(@Mocked ExpectativaCollaborator mock) {
        new Expectations() {{
            mock.methodForArgThat(withArgThat(new BaseMatcher<Object>() {
                @Override
                public boolean matches(Object item) {
                    return item instanceof Modelo && "info".equals(((Modelo) item).getInfo());
                }

                @Override
                public void describeTo(Description description) {
                }
            }));
        }};
        mock.methodForArgThat(new Modelo());
    }

    @Test
    public void testResultAndReturns(@Mocked ExpectativaCollaborator mock) {
        new Expectations() {{
            mock.methodReturnsString();
            result = "foo";
            result = new Exception();
            result = "bar";
            returns("foo", "bar");
            mock.methodReturnsInt();
            result = new int[]{1, 2, 3};
            result = 1;
        }};

        assertEquals("Should return foo", "foo", mock.methodReturnsString());
        try {
            mock.methodReturnsString();
            fail("Shouldn't reach here");
        } catch (Exception e) {
            // NOOP
        }
        assertEquals("Should return bar", "bar", mock.methodReturnsString());
        assertEquals("Should return 1", 1, mock.methodReturnsInt());
        assertEquals("Should return 2", 2, mock.methodReturnsInt());
        assertEquals("Should return 3", 3, mock.methodReturnsInt());
        assertEquals("Should return foo", "foo", mock.methodReturnsString());
        assertEquals("Should return bar", "bar", mock.methodReturnsString());
        assertEquals("Should return 1", 1, mock.methodReturnsInt());
    }

    @Test
    public void testDelegate(@Mocked ExpectativaCollaborator mock) {
        new Expectations() {{
            mock.methodForDelegate(anyInt);

            result = new Delegate<Integer>() {
                int delegate(int i) throws Exception {
                    if (i < 3) {
                        return 5;
                    } else {
                        throw new Exception();
                    }
                }
            };
        }};

        assertEquals("Should return 5", 5, mock.methodForDelegate(1));
        try {
            mock.methodForDelegate(3);
            fail("Shouldn't reach here");
        } catch (Exception e) {
        }
    }

}
