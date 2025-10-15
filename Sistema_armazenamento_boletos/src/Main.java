import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Digite o boleto a ser registrado:  ");
        String boleto = input.nextLine();

        if(ValidacaoBoleto(boleto)){
            System.out.println("Este boleto esta correto");
        } else {
            System.out.println("Existe algum erro neste boleto");
        }

        input.close();

    }
    public static boolean ValidacaoBoleto(String codigoDeBarras) {
        codigoDeBarras = limparLetras(codigoDeBarras);
        if(!verificarQuantidadeDeCaracteres(codigoDeBarras)){
            System.out.println("Codigo de barras com tamanho invalido");
            return false;
        }
        boolean bloco1 = validarBloco(codigoDeBarras.substring(0,9),codigoDeBarras.charAt(9));
        boolean bloco2 = validarBloco(codigoDeBarras.substring(10,20),codigoDeBarras.charAt(20));
        boolean bloco3 = validarBloco(codigoDeBarras.substring(21,31),codigoDeBarras.charAt(31));

        String codigoDoBanco = codigoDeBarras.substring(0,3);
        if(!bancoValidos(codigoDoBanco)) return false;

        boolean dvGeralValido = validarDvGeral(codigoDeBarras);

        return bloco1 && bloco2 && bloco3 && dvGeralValido;
    }

    public static String limparLetras(String codigoDeBarra){
        return codigoDeBarra.replaceAll("\\D","");
    }

    public static boolean verificarQuantidadeDeCaracteres(String codigoDeBarra){
        if(codigoDeBarra.length() != 47 && codigoDeBarra.length() != 48){ return false; }
        return true;
    }

    public static int calcularModulo10(String numeros){
        int soma = 0;
        int peso = 2;
        for(int i = numeros.length()-1; i >= 0; i--){
            int multiplicacao = (numeros.charAt(i) - 48) * peso;
            soma += (multiplicacao > 9)? multiplicacao - 9 : multiplicacao;
            peso = (peso == 2) ? 1: 2;
        }
        int resto = soma % 10;
        return (resto ==0) ? 0:10 - resto;
    }

    public static int calcularModulo11(String codigoDeBarra){
        int peso = 2;
        int soma = 0;


        for(int i = codigoDeBarra.length()-1; i >= 0; i--){
            soma += (codigoDeBarra.charAt(i) - 48) * peso;
            peso = (peso == 9) ? 2:peso + 1;
        }
        int resto = soma % 11;
        int resultado = 11 - resto;

        if(resultado == 0 || resultado == 10 || resultado == 11) return 1;//regra bancaria: DV = 1 nesses casos

        return resultado;
    }


    public static boolean validarBloco(String bloco, char dv){
        int dvCalculado = calcularModulo10(bloco);
        return dvCalculado == Character.getNumericValue(dv);
    }

    public static boolean validarDvGeral(String codigoDeBarra){
        String codigoDeBarrasReorganizado =
                codigoDeBarra.substring(0, 4) +     // Banco + moeda
                        codigoDeBarra.substring(33, 47) +   // Campo livre (último bloco)
                        codigoDeBarra.substring(4, 9) +     // Campo 1 (sem DV)
                        codigoDeBarra.substring(10, 20) +   // Campo 2 (sem DV)
                        codigoDeBarra.substring(21, 31);    // Campo 3 (sem DV)

        char dv = codigoDeBarra.charAt(32);//DV geral
        int dvCalculado = calcularModulo11(codigoDeBarrasReorganizado);

        return dvCalculado == Character.getNumericValue(dv);
    }

    public static boolean bancoValidos(String codigo){
        //tenho que ver uma maneira melhor de validar os banco no lugar de apenas colocar o codigo deles
        //talvez utilize POO depois para fazer a adição dos bancos, mas por enquanto vou colocar apenas alguns bancos

        String[] bancos = {"001", "033", "104", "237", "341", "356", "399", "745"};
        for(String banco : bancos){
            if(banco.equals(codigo)) return true;
        }
        return false;
    }



}

