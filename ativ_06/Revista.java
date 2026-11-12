public class Revista extends ItemBiblioteca {

    private final int edicao;

    public Revista(int codigo, String titulo, int edicao) {
        super(codigo, titulo);
        this.edicao = edicao;
    }

    @Override
    public int getPrazoDias() {
        return 7;
    }

    @Override
    public double getMultaPorDia() {
        return 1.00;
    }

    @Override
    public String getTipo() {
        return "Revista";
    }

    public int getEdicao() {
        return edicao;
    }
}
