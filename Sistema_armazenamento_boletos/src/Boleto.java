import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public  class Boleto {
    private final LocalDate dataVencimento;
    private final double valor;

    public Boleto(LocalDate dataVencimento, double valor) {
        this.dataVencimento = dataVencimento;
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String vencStr = (dataVencimento != null) ? dataVencimento.format(fmt) : "Sem vencimento";
        return "Vencimento: " + vencStr + " | Valor: R$ " + String.format("%.2f", valor);
    }
}
