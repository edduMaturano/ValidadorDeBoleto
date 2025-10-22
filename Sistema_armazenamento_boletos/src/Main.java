import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Digite o boleto a ser registrado:  ");
        String boleto = input.nextLine();

        if (ManipuladorDeBoletos.validarLinhaDigitavel(boleto)) {
            System.out.println("Boleto válido!");
            Boleto info = ManipuladorDeBoletos.extrairInformacoes(boleto);
            System.out.println(info);
        } else {
            System.out.println(" Boleto inválido!");


            input.close();
        }
    }
}
