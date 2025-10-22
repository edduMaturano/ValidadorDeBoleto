import java.time.LocalDate;

public class ManipuladorDeBoletos {

    //DFator de vencimento da FEBRABAN mais recente
    private static final LocalDate DATA_BASE = LocalDate.of(2025, 2, 22);

    public static boolean validarLinhaDigitavel(String codigoDeBarras) {
        codigoDeBarras = limparEspacos(codigoDeBarras);
        if (codigoDeBarras.length() != 47 || !codigoDeBarras.matches("\\d+")) return false;

        // Extrai campos
        String campo1 = codigoDeBarras.substring(0, 9);
        int dv1 = Character.getNumericValue(codigoDeBarras.charAt(9));
        String campo2 = codigoDeBarras.substring(10, 20);
        int dv2 = Character.getNumericValue(codigoDeBarras.charAt(20));
        String campo3 = codigoDeBarras.substring(21, 31);
        int dv3 = Character.getNumericValue(codigoDeBarras.charAt(31));
        int dvGeral = Character.getNumericValue(codigoDeBarras.charAt(32));

        // Valida DVs individuais (módulo 10)
        if (calcularModulo10(campo1) != dv1) return false;
        if (calcularModulo10(campo2) != dv2) return false;
        if (calcularModulo10(campo3) != dv3) return false;

        // Monta código de barras e valida DV geral (módulo 11)
        String barra = montarCodigoDeBarras(codigoDeBarras);
        String semDV = barra.substring(0, 4) + barra.substring(5);
        if (calcularModulo11(semDV) != dvGeral) return false;

        return true;
    }

    //Extrai informações do boleto, o valor, e data de vencimento
    public static Boleto extrairInformacoes(String codigoDeBarras) {
        codigoDeBarras = limparEspacos(codigoDeBarras);
        String barra = (codigoDeBarras.length() == 47) ? montarCodigoDeBarras(codigoDeBarras)
                : (codigoDeBarras.length() == 44 ? codigoDeBarras : null);

        if (barra == null) throw new IllegalArgumentException("Código inválido (deve ter 44 ou 47 dígitos).");

        String fatorStr = barra.substring(5, 9);
        String valorStr = barra.substring(9, 19);

        double valor = Double.parseDouble(valorStr) / 100.0;
        LocalDate vencimento = calcularDataVencimento(fatorStr);

        return new Boleto(vencimento, valor);
    }

    //limpar os espaço em branco e os " - "
    public static String limparEspacos(String codigoDeBarra) {
        return codigoDeBarra.replaceAll("[\\s-]+", "");
    }

    //Faz o caldo do modulo 10
    private static int calcularModulo10(String numero) {
        int soma = 0, multiplicador = 2;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(numero.charAt(i)) * multiplicador;
            soma += (n > 9) ? n - 9 : n;
            multiplicador = (multiplicador == 2) ? 1 : 2;
        }
        int resto = soma % 10;
        return (resto == 0) ? 0 : 10 - resto;
    }
       //Faz o caldo do modulo 11
    private static int calcularModulo11(String numero) {
        int soma = 0, multiplicador = 2;
        for (int i = numero.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(numero.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 9) ? 2 : multiplicador + 1;
        }
        int resto = soma % 11;
        if (resto == 0 || resto == 1 || resto == 10) return 1;
        return 11 - resto;
    }

    
    public static String montarCodigoDeBarras(String linha) {

        String bancoEMoeda = linha.substring(0, 4);
        String dvGeral = linha.substring(32, 33);
        String fatorEValor = linha.substring(33, 47);
        String campoLivre = linha.substring(4, 9) + linha.substring(10, 20) + linha.substring(21, 31);

        String codigoDeBarras = bancoEMoeda + dvGeral + fatorEValor + campoLivre;

        if (codigoDeBarras.length() != 44) {
            throw new IllegalStateException("Erro ao montar código de barras. Deve ter 44 dígitos.");
        }

        return codigoDeBarras;
    }

    //Faz o caulculo da data de vencimento
    private static LocalDate calcularDataVencimento(String fatorStr) {
        if (fatorStr.equals("0000")) return null;
        int fator = Integer.parseInt(fatorStr);
        return DATA_BASE.plusDays(fator - 1000);

    }

}

