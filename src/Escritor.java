import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Escritor {
    public static void main(String[] args) {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        try {
            String linea = entrada.readLine();
            if (linea == null) {
                System.err.println("No se recibió ningún número.");
                return;
            }

            int numero = Integer.parseInt(linea.trim());
            for (int incremento = 1; incremento <= 10; incremento++) {
                System.out.println(numero + incremento);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer el número: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("El dato recibido no es un número entero válido.");
        }
    }
}
