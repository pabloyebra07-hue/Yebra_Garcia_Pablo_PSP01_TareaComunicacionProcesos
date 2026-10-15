import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Tuberia {
    private static final String SEPARADOR = "--";

    public static void main(String[] args) {
        int indiceSeparador = buscarSeparador(args);
        if (indiceSeparador <= 0 || indiceSeparador == args.length - 1) {
            mostrarUso();
            return;
        }

        try {
            ProcessBuilder constructorPrimero = new ProcessBuilder(args[0]);
            for (int i = 1; i < indiceSeparador; i++) {
                constructorPrimero.command().add(args[i]);
            }
            constructorPrimero.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process procesoPrimero = constructorPrimero.start();

            ProcessBuilder constructorSegundo = new ProcessBuilder(args[indiceSeparador + 1]);
            for (int i = indiceSeparador + 2; i < args.length; i++) {
                constructorSegundo.command().add(args[i]);
            }

            constructorSegundo.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            constructorSegundo.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process procesoSegundo = constructorSegundo.start();


            BufferedReader salidaPrimero = new BufferedReader(
                    new InputStreamReader(procesoPrimero.getInputStream()));
            BufferedWriter entradaSegundo = new BufferedWriter(
                    new OutputStreamWriter(procesoSegundo.getOutputStream()));
            String linea;
            while ((linea = salidaPrimero.readLine()) != null) {
                entradaSegundo.write(linea);
                entradaSegundo.newLine();
                entradaSegundo.flush();
            }
            salidaPrimero.close();
            entradaSegundo.close();

            int codigoPrimero = procesoPrimero.waitFor();
            int codigoSegundo = procesoSegundo.waitFor();
            if (codigoPrimero != 0 || codigoSegundo != 0) {
                System.err.println("Alguno de los comandos terminó con error. Códigos: "
                        + codigoPrimero + ", " + codigoSegundo);
            }
        } catch (IOException e) {
            System.err.println("No se pudieron ejecutar los comandos: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("La ejecución fue interrumpida.");
        }

        System.out.println("Finalizada la ejecución");
    }

    private static int buscarSeparador(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (SEPARADOR.equals(args[i])) {
                return i;
            }
        }
        return -1;
    }

    private static void mostrarUso() {
        System.err.println("Uso: java Tuberia <comando1> [argumentos...] -- <comando2> [argumentos...]");
        System.err.println("Ejemplo: java Tuberia CMD /C DIR -- CMD /C FINDSTR /I java");
    }
}
