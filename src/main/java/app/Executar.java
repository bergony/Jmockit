package app;

public class Executar {

    private Colaborador colaborador;

    public void execute(Modelo modelo){
        boolean value = colaborador.colaborar(modelo.getInfo());
        colaborador.receber(value);
    }
}
