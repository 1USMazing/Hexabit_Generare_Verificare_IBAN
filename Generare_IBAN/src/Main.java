import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static String generare_IBAN_artificial()
    {
        //IBAN = 24 caractere
        // Cod tara. Ex pentru Romania: RO
        String cod_tara = "RO";

        // Codul de verificare (2 pozitii) - setat la 00
        String caracter_verificare = "00";

        // Codul bancii (4 cifre)
        // Cod Swift Raiffeisen
        String cod_swift = "RZBRROBUXXX";
        String cod_banca = cod_swift.substring(0, 4);

        if (cod_banca.length() != 4)
            throw new IllegalArgumentException("Codul bancii nu are 4 caractere");

        // Numarul contului bancar
        String alfanumerice = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder cont_bancar = new StringBuilder();

        for (int i=0; i<16; i++)
        {
            int poz_random = random.nextInt(alfanumerice.length());
            cont_bancar.append(alfanumerice.charAt(poz_random));
        }

        if (cont_bancar.length() != 16)
            throw new IllegalArgumentException("Contul bancar nu are 16 caractere");

        String iban = cod_tara + caracter_verificare + cod_banca + cont_bancar;

        if (iban.length() != 24)
            throw new IllegalArgumentException("Codul IBAN nu are 24 de caractere");

        return iban;
    }

    public static String verificare_iban(String iban)
    {
        if (iban.length() != 24)
            throw new IllegalArgumentException("Codul IBAN nu are 24 de caractere");

        // Pasul 1:  mutam primele 4 caractere la sfarsit
        String iban_prefix = iban.substring(0, 4);
        String iban_verificare = iban.substring(4) + iban_prefix;

        System.out.println("IBAN pasul 1 verificare: " + iban_verificare);

        // Pasul 2: calculare a caracterelor de verificare
        // A = 10, B = 11, ... Z = 35
        StringBuilder iban_conversie = new StringBuilder();

        for (char c : iban_verificare.toCharArray())
        {
            if (Character.isDigit(c))
                iban_conversie.append(c);
            else if (Character.isLetter(c))
            {
                int code = (int) c - 55;
                String cod = Integer.toString(code);
                iban_conversie.append(cod);
            }}

        System.out.println("IBAN pasul 2 conversie: " + iban_conversie);

        // Pasul 3: aplicare algoritmul MOD 97-10
        BigInteger nr_conversie = new BigInteger(iban_conversie.toString());

        // Impartin la 97 si extragem restul
        int rest_mod97 = nr_conversie.mod(BigInteger.valueOf(97)).intValue();

        System.out.println("Restul impartii la 97: " + rest_mod97);

        //Scadem restul din 98
        int rest_din_98 = 98 - rest_mod97;

        System.out.println("Restul scaderii din 98: " + rest_din_98);

        // Inlocuim in IBAN codul de verificare
        String iban_final = iban.substring(0,2) + rest_din_98 + iban.substring(4);

        if (iban_final.length() != 24)
            throw new IllegalArgumentException("Codul IBAN final nu are 24 de caractere");

        return iban_final;
    }

    public static void main(String[] args) {

        String iban = generare_IBAN_artificial();
        String iban_test = "RO00AAAA1B31007593840000";
        System.out.println("Generare IBAN artificial: " + iban_test);

        //String iban_test = "RO00AAAA1B31007593840000";

        String iban_final = verificare_iban(iban_test);
        System.out.println("IBAN final: " + iban_final);
    }
}