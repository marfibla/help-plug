import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    println("--- INICIANT MONITOR D'ERRORS ---")

    val comanda = listOf("cmd.exe", "/c", "echo Segmentation fault (core dumped)")

    try {
        val processBuilder = ProcessBuilder(comanda)
        processBuilder.redirectErrorStream(true)
        val proces = processBuilder.start()

        val lector = BufferedReader(InputStreamReader(proces.inputStream))
        var linia: String?
        var sHaDetectatError = false

        println("Executant comanda i esperant sortida...")

        while (lector.readLine().also { linia = it } != null) {
            println("-> Sortida del programa: $linia")

            if (linia?.contains("Segmentation fault", ignoreCase = true) == true) {
                sHaDetectatError = true
            }
        }

        proces.waitFor()

        if (sHaDetectatError) {
            println("\nALERTA DETECTADA: Segmentation Fault!")
            println("Iniciant el proces de generacio d'imatge...")

            generarImatgeDArtAbstracte("Un bolcatge de memoria caotic representat com art abstracte digital amb colors neo i text binari trencat")

        } else {
            println("\nEl programa ha acabat sense errors de memoria critics.")
        }

    } catch (e: Exception) {
        println("Error intentant executar el proces: ${e.message}")
    }
}

fun generarImatgeDArtAbstracte(prompt: String) {
    println("Simulant crida a l'API de IA amb el prompt: '$prompt'...")
    println("Imatge generada correctament.")
}