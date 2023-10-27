import android.content.Context
import android.content.SharedPreferences

class SharedPreferences {
    companion object {
        private const val NOMBRE = "Kimo"

        fun GuardarBool(context: Context, nombre: String, dato: Boolean) {
            val archivo: SharedPreferences = context.getSharedPreferences(NOMBRE, Context.MODE_PRIVATE)
            val editor = archivo.edit()
            editor.putBoolean(nombre, dato)
            editor.apply()
        }

        fun CargarBool(context: Context, nombre: String): Boolean {
            val archivo: SharedPreferences = context.getSharedPreferences(NOMBRE, Context.MODE_PRIVATE)
            return archivo.getBoolean(nombre, false)
        }
    }
}