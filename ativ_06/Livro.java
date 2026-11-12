public class Livro extends ItemBiblioteca {

    private final String autor;

    public Livro(int codigo, String titulo, String autor) {
        super(codigo, titulo);
        this.autor = autor;
    }

    @Override
    public int getPrazoDias() {
        return 14;
    }

    @Override
    public double getMultaPorDia() {
        return 0.50;
    }

    @Override
    public String getTipo() {
        return "Livro";
    }

    public String getAutor() {
        return autor;
    }
}
